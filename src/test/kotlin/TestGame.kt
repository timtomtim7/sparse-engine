import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.window.input.MouseButton
import blue.sparse.math.vectors.floats.Vector2f

class TestGame : SparseGame()
{
	//	val vbo: Int
	val shader: ShaderProgram
	val array: VertexArray
	val texture: Texture

	val indexedModel: IndexedModel

	init
	{
		texture = Texture(Asset["textures/developer/diffuse.png"])
		val layout = VertexLayout()
		layout.add<Vector2f>()
		layout.add<Vector2f>()

		val buffer = VertexBuffer()
		buffer.add(
				Vector2f(-1f, -1f), Vector2f(0f, 1f),
				Vector2f(-1f, 1f), Vector2f(0f, 0f),
				Vector2f(1f, -1f), Vector2f(1f, 1f),
				Vector2f(1f, 1f), Vector2f(1f, 0f)
		)

		array = VertexArray()
		array.add(buffer, layout)

		shader = ShaderProgram(
				fragment = Asset["shaders/fragment.fs"],
				vertex = Asset["shaders/vertex.vs"]
		)

		indexedModel = IndexedModel(array, intArrayOf(
				0, 1, 3,
				0, 3, 2
		))
	}

	override fun update(delta: Float)
	{
		if(input[MouseButton.LEFT].pressed)
			println("Left")

		if(input[MouseButton.RIGHT].pressed)
			println("Right")
	}

	override fun render(delta: Float)
	{
		bind(shader, texture) {
			indexedModel.render()
		}
	}
}