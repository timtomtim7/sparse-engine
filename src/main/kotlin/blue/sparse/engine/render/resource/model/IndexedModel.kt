package blue.sparse.engine.render.resource.model

import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.resource.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

@Deprecated("Replaced", ReplaceWith("array.setIndices(indices).toModel()"), DeprecationLevel.ERROR)
class IndexedModel(val array: VertexArray, indices: IntArray) : Model, Resource(), Bindable {
	internal val id: Int
	val size = indices.size

	init {
		id = glCall { glGenBuffers() }

		val buffer = ByteBuffer.allocateDirect(indices.size * 4).order(ByteOrder.nativeOrder())
		indices.forEach { buffer.putInt(it) }
		buffer.flip()

		bind()
		glCall { glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW) }
//		unbind()
//		bind { glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW) }
	}

	override fun bind() {
		array.bind()
		glCall { glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id) }
	}

	override fun unbind() {
		glCall { glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0) }
	}

	override fun render() {
		bind(array, this) {
			glCall { glDrawElements(GL_TRIANGLES, size, GL_UNSIGNED_INT, 0) }
		}
	}

	override fun release() {
		glCall { glDeleteBuffers(id) }
	}
}