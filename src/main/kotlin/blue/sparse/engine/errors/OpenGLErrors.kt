package blue.sparse.engine.errors

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION

fun clearOpenGLErrors() {
	do while(glGetError() != GL_NO_ERROR)
}

enum class GLError(val id: Int) {
	INVALID_ENUM					(GL_INVALID_ENUM),
	INVALID_VALUE					(GL_INVALID_VALUE),
	INVALID_OPERATION				(GL_INVALID_OPERATION),
	INVALID_FRAMEBUFFER_OPERATION	(GL_INVALID_FRAMEBUFFER_OPERATION),
	OUT_OF_MEMORY					(GL_OUT_OF_MEMORY);

	override fun toString(): String {
		return "$name (0x${id.toString(16)})"
	}

	companion object {
		operator fun get(id: Int): GLError? {
			return values().find { it.id == id }
		}
	}
}

private fun accumulateErrors(initial: Int): String {
	val errors = ArrayList<GLError>()

	var errorID = initial
	while(errorID != GL_NO_ERROR) {
		errors.add(GLError[errorID] ?: continue)
		errorID = glGetError()
	}

	return errors.toString()

//	val string = StringBuilder("[")
//
//	var error = initial
//	while (error != GL_NO_ERROR) {
//		string.append(", ")
//		val name = when(error) {
//			GL_INVALID_ENUM -> "GL_INVALID_ENUM"
//			GL_INVALID_VALUE -> "GL_INVALID_VALUE"
//			GL_INVALID_OPERATION -> "GL_INVALID_OPERATION"
//			GL_INVALID_FRAMEBUFFER_OPERATION -> "GL_INVALID_FRAMEBUFFER_OPERATION"
//			GL_OUT_OF_MEMORY -> "GL_OUT_OF_MEMORY"
//			else -> "UNKNOWN"
//		}
//		string.append(name)
//		string.append(" (0x${error.toString(16)})")
//
//		error = glGetError()
//	}
//
//	string.append(']')
//	return string.toString()
}

fun getOpenGLErrors(): String? {
	val error = glGetError()
	if (error != GL_NO_ERROR)
		return accumulateErrors(error)
	return null
}

fun printOpenGLErrors(): Boolean {
	getOpenGLErrors()?.let {
		println("OpenGL Errors: $it")
		return true
	}
	return false
}

class OpenGLError(message: String) : RuntimeException(message)

//inline fun <R> glCall(body: () -> R): R
//{
//	return body()
//}

inline fun <R> glCall(body: () -> R): R {
	clearOpenGLErrors()
	val result = body()
	getOpenGLErrors()?.let { throw OpenGLError(it) }

	return result
}