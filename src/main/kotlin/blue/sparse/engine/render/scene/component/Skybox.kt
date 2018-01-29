package blue.sparse.engine.render.scene.component

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.vectors.floats.Vector2f
import blue.sparse.math.vectors.floats.Vector3f
import org.lwjgl.opengl.GL11.*

class Skybox(private val texture: Texture) : Component
{
	private val model: Model
	override val overridesShader = true

	constructor(asset: Asset) : this(Texture(asset))

	init
	{
		val layout = VertexLayout()
		layout.add<Vector3f>()
		layout.add<Vector2f>()

		val ax = -5.001f
		val ay = -5.001f
		val az = -5.001f
		val bx = 5.001f
		val by = 5.001f
		val bz = 5.001f
		val cx = -5f
		val cy = -5f
		val cz = -5f
		val dx = 5f
		val dy = 5f
		val dz = 5f

		val t = 1.0f / 4.0f

		val buffer = VertexBuffer()

		//BOTTOM
		buffer.add(Vector3f(ax, cy, az), Vector2f(t * 4, t * 2))
		buffer.add(Vector3f(bx, cy, az), Vector2f(t * 3, t * 2))
		buffer.add(Vector3f(bx, cy, bz), Vector2f(t * 3, t * 1))
		buffer.add(Vector3f(ax, cy, bz), Vector2f(t * 4, t * 1))

		//TOP
		buffer.add(Vector3f(ax, dy, az), Vector2f(t * 1, t * 2))
		buffer.add(Vector3f(ax, dy, bz), Vector2f(t * 1, t * 1))
		buffer.add(Vector3f(bx, dy, bz), Vector2f(t * 2, t * 1))
		buffer.add(Vector3f(bx, dy, az), Vector2f(t * 2, t * 2))

		//BACK
		buffer.add(Vector3f(ax, ay, cz), Vector2f(t * 1, t * 3))
		buffer.add(Vector3f(ax, by, cz), Vector2f(t * 1, t * 2))
		buffer.add(Vector3f(bx, by, cz), Vector2f(t * 2, t * 2))
		buffer.add(Vector3f(bx, ay, cz), Vector2f(t * 2, t * 3))

		//FRONT
		buffer.add(Vector3f(ax, ay, dz), Vector2f(t * 1, t * 0))
		buffer.add(Vector3f(bx, ay, dz), Vector2f(t * 2, t * 0))
		buffer.add(Vector3f(bx, by, dz), Vector2f(t * 2, t * 1))
		buffer.add(Vector3f(ax, by, dz), Vector2f(t * 1, t * 1))

		//LEFT
		buffer.add(Vector3f(cx, ay, az), Vector2f(t * 0, t * 2))
		buffer.add(Vector3f(cx, ay, bz), Vector2f(t * 0, t * 1))
		buffer.add(Vector3f(cx, by, bz), Vector2f(t * 1, t * 1))
		buffer.add(Vector3f(cx, by, az), Vector2f(t * 1, t * 2))

		//RIGHT
		buffer.add(Vector3f(dx, ay, az), Vector2f(t * 3, t * 2))
		buffer.add(Vector3f(dx, by, az), Vector2f(t * 2, t * 2))
		buffer.add(Vector3f(dx, by, bz), Vector2f(t * 2, t * 1))
		buffer.add(Vector3f(dx, ay, bz), Vector2f(t * 3, t * 1))

		val array = VertexArray()
		array.add(buffer, layout)

		model = IndexedModel(array, intArrayOf(
				2, 1, 0,
				0, 3, 2,

				2 + 4, 1 + 4, 0 + 4,
				0 + 4, 3 + 4, 2 + 4,

				2 + 8, 1 + 8, 0 + 8,
				0 + 8, 3 + 8, 2 + 8,

				2 + 12, 1 + 12, 0 + 12,
				0 + 12, 3 + 12, 2 + 12,

				2 + 16, 1 + 16, 0 + 16,
				0 + 16, 3 + 16, 2 + 16,

				2 + 20, 1 + 20, 0 + 20,
				0 + 20, 3 + 20, 2 + 20
		))
	}

	override fun render(delta: Float, camera: Camera, shader: ShaderProgram)
	{
		glCall { glDisable(GL_DEPTH_TEST) }
		glCall { glDepthMask(false) }

		bind(Companion.shader, texture) {
			Companion.shader.uniforms["uProjection"] = camera.projection
			Companion.shader.uniforms["uRotation"] = camera.transform.rotation.conjugate.toMatrix()
			model.render()
		}

		glCall { glDepthMask(true) }
		glCall { glEnable(GL_DEPTH_TEST) }
	}

	companion object
	{
		private val shader = ShaderProgram(Asset["shaders/skybox.fs"], Asset["shaders/skybox.vs"])
	}
}