import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.window.input.MouseButton
import blue.sparse.math.vectors.floats.Vector2f
import blue.sparse.math.vectors.floats.Vector3f

class TestGame : SparseGame()
{
	//	val vbo: Int
	val shader: ShaderProgram
	val array: VertexArray

	init
	{
//		val layout = VertexLayout()
//		layout.add<Vector2f>()
//		layout.add<Vector3f>()

		val positionLayout = VertexLayout()
		positionLayout.add<Vector2f>()

		val colorLayout = VertexLayout()
		colorLayout.add<Vector3f>()

//		val buffer = VertexBuffer()
//		buffer.add(
//				Vector2f(-1f, -1f), Vector3f(1f, 0f, 0f),
//				Vector2f(0f, 1f), Vector3f(0f, 1f, 0f),
//				Vector2f(1f, -1f), Vector3f(0f, 0f, 1f)
//		)

		val positions = VertexBuffer()
		positions.add(
				Vector2f(-1f,  -1f),
				Vector2f(0f,  1f),
				Vector2f(1f,  -1f)
		)

		val colors = VertexBuffer()
		colors.add(
				Vector3f(1f, 0f, 0f),
				Vector3f(0f, 1f, 0f),
				Vector3f(0f, 0f, 1f)
		)

		array = VertexArray()
		array.add(positions, positionLayout)
		array.add(colors, colorLayout)
		//array.add(buffer, layout)

		shader = ShaderProgram(
				fragment = Asset["fragment.fs"],
				vertex = Asset["vertex.vs"]
		)
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
		shader.bind { array.render() }
	}
}