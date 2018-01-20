package blue.sparse.engine.render.resource.model

import blue.sparse.engine.render.resource.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class IndexedModel(val array: VertexArray, indices: IntArray) : Model, Resource(), Bindable
{
	internal val id: Int
	val size = indices.size

	init
	{
		id = glGenBuffers()

		val buffer = ByteBuffer.allocateDirect(indices.size * 4).order(ByteOrder.nativeOrder())
		indices.forEach { buffer.putInt(it) }
		buffer.flip()

		bind()
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW)
		unbind()
//		bind { glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW) }
	}

	override fun bind()
	{
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id)
	}

	override fun unbind()
	{
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0)
	}

	override fun render()
	{
		bind(array, this) {
			glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0)
		}
	}

	override fun release()
	{
		glDeleteBuffers(id)
	}
}