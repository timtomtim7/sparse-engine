package blue.sparse.engine.render.resource.model

import blue.sparse.engine.render.resource.*
import blue.sparse.engine.util.Size
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*

class VertexArray : Resource(), Bindable
{
	internal val id: Int

	private var attributeCount = 0
	private val buffers = ArrayList<Int>()

	var size = 0
		private set

	init
	{
		id = glGenVertexArrays()
	}

	fun add(buffer: VertexBuffer, layout: VertexLayout)
	{
		bind()
		if (!buffer.checkLayout(layout))
			throw IllegalArgumentException("Provided buffer does not conform to the layout.")

		if(!buffers.isEmpty() && size != buffer.size / layout.size)
			throw IllegalArgumentException("Number of elements in buffer must be consistent ($size)")

		size = buffer.size / layout.size

		val bufferId = glGenBuffers()
		buffers.add(bufferId)

		glBindBuffer(GL_ARRAY_BUFFER, bufferId)
		glBufferData(GL_ARRAY_BUFFER, buffer.toByteBuffer(), GL_STATIC_DRAW)

		var pointer = 0L

		val classes = layout.layout
		for (clazz in classes)
		{
			val size = Size.of(clazz)

			//TODO: Do not assume it's a `GL_FLOAT`! Currently ints can be added as attributes but will not work as intended.

			glEnableVertexAttribArray(attributeCount)
			glVertexAttribPointer(attributeCount, size / 4, GL_FLOAT, false, layout.size, pointer)

			pointer += size
			attributeCount++
		}
	}

	override fun bind()
	{
		glBindVertexArray(id)
	}

	override fun unbind()
	{
		glBindVertexArray(0)
	}

	fun render()
	{
		bind {
			glDrawArrays(GL_TRIANGLES, 0, size)
		}
	}

	override fun release()
	{
		glDeleteVertexArrays(id)
		buffers.forEach(::glDeleteBuffers)
	}
}