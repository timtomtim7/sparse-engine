package blue.sparse.engine.render.resource.model

import blue.sparse.engine.render.resource.Resource
import blue.sparse.engine.render.resource.bind
import org.lwjgl.opengl.GL11.GL_TRIANGLES
import org.lwjgl.opengl.GL11.glDrawArrays
import org.lwjgl.opengl.GL15.*

class BasicModel(vertices: List<Vertex>, layout: VertexLayout) : Resource(), Model
{
	internal val id: Int
	val vertexCount: Int

	init
	{
		id = glGenBuffers()
		vertexCount = vertices.size

		bind()
	}

	override fun render()
	{
		bind {
			glDrawArrays(GL_TRIANGLES, 0, vertexCount)
		}
	}

	override fun bind()
	{
		glBindBuffer(GL_ARRAY_BUFFER, id)
	}

	override fun unbind()
	{
	}

	override fun release()
	{
	}
}