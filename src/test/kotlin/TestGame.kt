import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.camera.FirstPerson
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.vectors.floats.Vector2f
import blue.sparse.math.vectors.floats.Vector3f

class TestGame : SparseGame()
{
	private val shader: ShaderProgram
	private val texture: Texture = Texture(Asset["textures/developer/diffuse.png"])

	private val indexedModel: IndexedModel

	//	private val camera = Camera.orthographic(-4f, 4f, -2.25f, 2.25f, -100f, 100f)
	private val camera = Camera.perspective(100f, 16f / 9f, 0.1f, 1000f)

	init
	{
		val layout = VertexLayout()
		layout.add<Vector3f>()
		layout.add<Vector2f>()

		val depth = -1f

		val buffer = VertexBuffer()
		buffer.add(
				Vector3f(-1f, -1f, depth), Vector2f(0f, 1f),
				Vector3f(-1f, 1f, depth), Vector2f(0f, 0f),
				Vector3f(1f, -1f, depth), Vector2f(1f, 1f),
				Vector3f(1f, 1f, depth), Vector2f(1f, 0f)
		)

		val array = VertexArray()
		array.add(buffer, layout)

		shader = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
		indexedModel = IndexedModel(array, intArrayOf(0, 1, 3, 0, 3, 2))

		camera.transform.translate(Vector3f(0f, 0f, -10f))
		camera.controller = FirstPerson(camera)
	}

	override fun update(delta: Float)
	{
//		if(input[MouseButton.LEFT].pressed) println("Left!")
//		if(input[MouseButton.RIGHT].pressed) println("Right!")
//		if(input[Key.ESCAPE].pressed) println("Escape!")

		camera.update(delta)
//		camera.transform.rotateDeg(camera.transform.rotation.right, 5 * delta)
//		camera.transform.rotateDeg(camera.transform.rotation.up, 30 * delta)
	}

	override fun render(delta: Float)
	{
		bind(shader, texture) {
			//			shader.uniforms["model"] = Matrix4f.identity()
			shader.uniforms["viewProj"] = camera.viewProjectionMatrix
			indexedModel.render()
		}
	}
}