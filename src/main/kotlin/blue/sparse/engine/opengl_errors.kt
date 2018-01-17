package blue.sparse.engine

import org.lwjgl.opengl.GL11.GL_NO_ERROR
import org.lwjgl.opengl.GL11.glGetError

fun getOpenGLErrors(): String?
{
	val errors = ArrayList<Int>()
	var error = glGetError()
	while (error != GL_NO_ERROR)
	{
		errors.add(error)
		error = glGetError()
	}

	if(errors.isEmpty()) return null
	return errors.toString()
}

fun printOpenGLErrors()
{
	getOpenGLErrors()?.let {
		println("OpenGL Errors: $it")
	}
}