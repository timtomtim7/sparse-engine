package blue.sparse.engine.render.scene.component

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.StateManager
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.vectors.floats.Vector2f
import blue.sparse.math.vectors.floats.Vector3f

class Skybox(private val texture: Texture) : Component {
	private val model: Model
	override val overridesShader = true

	constructor(asset: Asset) : this(Texture(asset))

	init {
		val layout = VertexLayout()
		layout.add<Vector3f>()
		layout.add<Vector2f>()

		val s = 1f

		val t = 1.0f / 4.0f
		val p = 0.001f

		val buffer = VertexBuffer()

		val nnn = Vector3f(-s, -s, -s)
		val nnp = Vector3f(-s, -s, +s)
		val npn = Vector3f(-s, +s, -s)
		val npp = Vector3f(-s, +s, +s)
		val pnn = Vector3f(+s, -s, -s)
		val pnp = Vector3f(+s, -s, +s)
		val ppn = Vector3f(+s, +s, -s)
		val ppp = Vector3f(+s, +s, +s)

//		val tn1 = t * (1 - p)
//		val tp1 = t * (1 + p)
//		val tn2 = t * (2 - p)
//		val tp2 = t * (2 + p)
//		val tn3 = t * (3 - p)
//		val tp3 = t * (3 + p)
//		val tn4 = t * (4 - p)
//		val tp4 = t * (4 + p)

		val tn1 = t * (1 - p)
		val tn2 = t * (2 - p)
		val tn3 = t * (3 - p)
		val tn4 = t * (4 - p)

		val tp0 = t * (0 + p)
		val tp1 = t * (1 + p)
		val tp2 = t * (2 + p)
		val tp3 = t * (3 + p)

		//BOTTOM
		buffer.add(nnn, Vector2f(tn4, tn2))
		buffer.add(pnn, Vector2f(tp3, tn2))
		buffer.add(pnp, Vector2f(tp3, tp1))
		buffer.add(nnp, Vector2f(tn4, tp1))

		//TOP
		buffer.add(npn, Vector2f(tp1, tn2))
		buffer.add(npp, Vector2f(tp1, tp1))
		buffer.add(ppp, Vector2f(tn2, tp1))
		buffer.add(ppn, Vector2f(tn2, tn2))

		//BACK
		buffer.add(nnn, Vector2f(tp1, tn3))
		buffer.add(npn, Vector2f(tp1, tp2))
		buffer.add(ppn, Vector2f(tn2, tp2))
		buffer.add(pnn, Vector2f(tn2, tn3))

		//FRONT
		buffer.add(nnp, Vector2f(tp1, tp0))
		buffer.add(pnp, Vector2f(tn2, tp0))
		buffer.add(ppp, Vector2f(tn2, tn1))
		buffer.add(npp, Vector2f(tp1, tn1))

		//LEFT
		buffer.add(nnn, Vector2f(tp0, tn2))
		buffer.add(nnp, Vector2f(tp0, tp1))
		buffer.add(npp, Vector2f(tn1, tp1))
		buffer.add(npn, Vector2f(tn1, tn2))

		//RIGHT
		buffer.add(pnn, Vector2f(tn3, tn2))
		buffer.add(ppn, Vector2f(tp2, tn2))
		buffer.add(ppp, Vector2f(tp2, tp1))
		buffer.add(pnp, Vector2f(tn3, tp1))

		val array = VertexArray()
		array.add(buffer, layout)

		val baseIndices = intArrayOf(2, 1, 0, 0, 3, 2)
		val indices = IntArray(6 * 6) { baseIndices[it % 6] + ((it / 6) * 4) }

		model = array.setIndices(indices).toModel()
	}

	override fun render(delta: Float, camera: Camera, shader: ShaderProgram) {
//		glCall { glDisable(GL_DEPTH_TEST) }
//		glCall { glDepthMask(false) }
		StateManager.depthTest = false
		StateManager.depthMask = false

		bind(Companion.shader, texture) {
			Companion.shader.uniforms["uProjection"] = camera.projection
			Companion.shader.uniforms["uRotation"] = camera.transform.rotation.conjugate.toMatrix()
			model.render()
		}

		StateManager.depthTest = true
		StateManager.depthMask = true
//		glCall { glDepthMask(true) }
//		glCall { glEnable(GL_DEPTH_TEST) }
	}

	companion object {
		private val shader = ShaderProgram(Asset["shaders/skybox.fs"], Asset["shaders/skybox.vs"])
	}
}