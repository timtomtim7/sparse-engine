package blue.sparse.engine.render.resource.model

import blue.sparse.engine.errors.glCall
import blue.sparse.engine.math.*
import blue.sparse.engine.render.resource.*
import blue.sparse.engine.util.Size
import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.bytes.*
import blue.sparse.math.vectors.floats.*
import blue.sparse.math.vectors.ints.*
import blue.sparse.math.vectors.shorts.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import org.lwjgl.opengl.GL20.glEnableVertexAttribArray
import org.lwjgl.opengl.GL20.glVertexAttribPointer
import org.lwjgl.opengl.GL30.*

class VertexArray(val primitive: GeometryPrimitive = GeometryPrimitive.TRIANGLES) : Resource(), Bindable
{
	internal val id: Int

	private var attributeCount = 0
	private val buffers = ArrayList<Int>()

	var size = 0
		private set

	init
	{
		id = glCall { glGenVertexArrays() }
	}

	fun add(buffer: VertexBuffer, layout: VertexLayout)
	{
		bind()
		if (!buffer.checkLayout(layout))
			throw IllegalArgumentException("Provided buffer does not conform to the layout.")

		if(!buffers.isEmpty() && size != buffer.size / layout.size)
			throw IllegalArgumentException("Number of elements in buffer must be consistent ($size)")

		size = buffer.size / layout.size

		val bufferId = glCall { glGenBuffers() }
		buffers.add(bufferId)

		glCall { glBindBuffer(GL_ARRAY_BUFFER, bufferId) }
		glCall { glBufferData(GL_ARRAY_BUFFER, buffer.toByteBuffer(), GL_STATIC_DRAW) }

		var pointer = 0L

		val classes = layout.layout
		for (clazz in classes)
		{
			val size = Size.of(clazz)

			glCall { glEnableVertexAttribArray(attributeCount) }

//			val type = getOpenGLType(clazz)
			val type = OpenGLType[clazz] ?: throw IllegalStateException("No OpenGLType for $clazz")

			glCall {
				if(type.isIntType)
					glVertexAttribIPointer(attributeCount, type.count, type.id, layout.size, pointer)
				else
					glVertexAttribPointer(attributeCount, type.count, type.id, false, layout.size, pointer)
			}

			pointer += size
			attributeCount++
		}
	}

	private enum class OpenGLType(val clazz: Class<*>, val id: Int, val count: Int, val isIntType: Boolean = false)
	{
		VECTOR2B(Vector2b::class.java, GL_BYTE, 2),
		VECTOR3B(Vector3b::class.java, GL_BYTE, 3),
		VECTOR4B(Vector4b::class.java, GL_BYTE, 4),

		VECTOR2S(Vector2s::class.java, GL_SHORT, 2),
		VECTOR3S(Vector3s::class.java, GL_SHORT, 3),
		VECTOR4S(Vector4s::class.java, GL_SHORT, 4),

		VECTOR2I(Vector2i::class.java, GL_INT, 2),
		VECTOR3I(Vector3i::class.java, GL_INT, 3),
		VECTOR4I(Vector4i::class.java, GL_INT, 4),

		VECTOR2F(Vector2f::class.java, GL_FLOAT, 2),
		VECTOR3F(Vector3f::class.java, GL_FLOAT, 3),
		VECTOR4F(Vector4f::class.java, GL_FLOAT, 4),

		MATRIX4F(Matrix4f::class.java, GL_FLOAT, 16),
		FLOAT(Float::class.java, GL_FLOAT, 1),
		DOUBLE(Double::class.java, GL_DOUBLE, 1),
		BYTE(java.lang.Byte::class.java, GL_BYTE, 1, true),
		SHORT(java.lang.Short::class.java, GL_SHORT, 1, true),
		INT(java.lang.Integer::class.java, GL_INT, 1, true),

		UBYTE(UByte::class.java, GL_UNSIGNED_BYTE, 1, true),
		USHORT(UShort::class.java, GL_UNSIGNED_SHORT, 1, true),
		UINT(UInt::class.java, GL_UNSIGNED_INT, 1, true);

		companion object
		{
			operator fun get(clazz: Class<*>) = values().find { it.clazz == clazz }
		}
	}

	override fun bind()
	{
		glCall { glBindVertexArray(id) }
	}

	override fun unbind()
	{
		glCall { glBindVertexArray(0) }
	}

	fun render()
	{
		bind {
			glCall { glDrawArrays(primitive.id, 0, size) }
		}
	}

	override fun release()
	{
		glCall { glDeleteVertexArrays(id) }
		buffers.forEach { glCall { glDeleteBuffers(it) } }
	}
}