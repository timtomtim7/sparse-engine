import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.window.input.MouseButton
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.*

class TestGame : SparseGame()
{
	var vbo: Int = 0
	lateinit var shader: ShaderProgram

	override fun init()
	{
		vbo = glGenBuffers()
		glBindBuffer(GL_ARRAY_BUFFER, vbo)
		glBufferData(GL_ARRAY_BUFFER, floatArrayOf(
				-1f, -1f,
				0f, 1f,
				1f, -1f
		), GL_STATIC_DRAW)

		shader = ShaderProgram(
				fragment = Asset["fragment.fs"],
				vertex = Asset["vertex.vs"]
		)
	}

	override fun update(delta: Float)
	{
		if(input[MouseButton.LEFT].pressed)
			println("Pressed")
		if(input[MouseButton.LEFT].released)
			println("Released")

		if(input.mouseMoved)
			println(input.mousePosition)
	}

	override fun render(delta: Float)
	{
		shader.bind {
			glBindBuffer(GL_ARRAY_BUFFER, vbo)
			glEnableVertexAttribArray(0)
			glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
			glDrawArrays(GL_TRIANGLES, 0, 3)
			glDisableVertexAttribArray(0)
		}
	}
}