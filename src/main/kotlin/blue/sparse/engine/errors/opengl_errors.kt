package blue.sparse.engine.errors

import org.lwjgl.opengl.GL11.GL_NO_ERROR
import org.lwjgl.opengl.GL11.glGetError

fun clearOpenGLErrors()
{
	while(glGetError() != GL_NO_ERROR) {}
}

private fun accumulateErrors(initial: Int): String
{
	val string = StringBuilder("[$initial")

	var error = glGetError()
	while (error != GL_NO_ERROR)
	{
		string.append(", ")
		string.append(error)

		error = glGetError()
	}

	string.append(']')
	return string.toString()
}

fun getOpenGLErrors(): String?
{
	val error = glGetError()
	if(error != GL_NO_ERROR)
		return accumulateErrors(error)
	return null
}

fun printOpenGLErrors(): Boolean
{
	getOpenGLErrors()?.let {
		println("OpenGL Errors: $it")
		return true
	}
	return false
}

class OpenGLError(message: String): RuntimeException(message)

//inline fun <R> glCall(body: () -> R): R
//{
//	return body()
//}

inline fun <R> glCall(body: () -> R): R
{
	clearOpenGLErrors()
	val result = body()
	getOpenGLErrors()?.let { throw OpenGLError(it) }

	return result
}