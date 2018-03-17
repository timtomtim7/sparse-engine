package blue.sparse.engine.render.scene.component

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.Shader
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.vectors.floats.Vector3f
import org.lwjgl.opengl.GL11.*

class ShaderSkybox(fragmentShader: Shader, private val uniformSetter: ShaderProgram.() -> Unit = {}) : Component
{
	private val model: Model
	override val overridesShader = true

	private val shader: ShaderProgram

	constructor(asset: Asset, uniformSetter: ShaderProgram.() -> Unit = {}): this(Shader(asset, Shader.Type.FRAGMENT), uniformSetter)

	init
	{
		if(fragmentShader.type != Shader.Type.FRAGMENT)
			throw IllegalArgumentException("Constructor argument `fragmentShader` was not a fragment shader.")

		shader = ShaderProgram(vertexShader, fragmentShader)

		val layout = VertexLayout()
		layout.add<Vector3f>()

		val s = 1f

		val buffer = VertexBuffer()

		val nnn = Vector3f(-s, -s, -s)
		val nnp = Vector3f(-s, -s, +s)
		val npn = Vector3f(-s, +s, -s)
		val npp = Vector3f(-s, +s, +s)
		val pnn = Vector3f(+s, -s, -s)
		val pnp = Vector3f(+s, -s, +s)
		val ppn = Vector3f(+s, +s, -s)
		val ppp = Vector3f(+s, +s, +s)

		buffer.add(nnn, pnn, pnp, nnp) //BOTTOM
		buffer.add(npn, npp, ppp, ppn) //TOP
		buffer.add(nnn, npn, ppn, pnn) //BACK
		buffer.add(nnp, pnp, ppp, npp) //FRONT
		buffer.add(nnn, nnp, npp, npn) //LEFT
		buffer.add(pnn, ppn, ppp, pnp) //RIGHT

		val array = VertexArray()
		array.add(buffer, layout)

		val baseIndices = intArrayOf(2, 1, 0, 0, 3, 2)
		val indices = IntArray(6*6) { baseIndices[it % 6] + ((it / 6) * 4) }

		model = IndexedModel(array, indices)
	}

	override fun render(delta: Float, camera: Camera, shader: ShaderProgram)
	{
		glCall { glDisable(GL_DEPTH_TEST) }
		glCall { glDepthMask(false) }

		this.shader.bind {
			uniforms["uProjection"] = camera.projection
			uniforms["uRotation"] = camera.transform.rotation.conjugate.toMatrix()
			uniformSetter()
			model.render()
		}

		glCall { glDepthMask(true) }
		glCall { glEnable(GL_DEPTH_TEST) }
	}

	companion object
	{
		private val vertexShader = Shader(Asset["shaders/shader_skybox.vs"], Shader.Type.VERTEX)
	}
}