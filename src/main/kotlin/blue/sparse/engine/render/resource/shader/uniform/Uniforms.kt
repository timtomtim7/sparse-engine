package blue.sparse.engine.render.resource.shader.uniform

import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.floats.*
import org.lwjgl.opengl.GL20.*

class Uniforms internal constructor(val program: ShaderProgram)
{
	private val uniforms = HashMap<String, Int>()

	private operator fun get(name: String): Int
	{
		return uniforms.getOrPut(name) {
			val result = glCall { glGetUniformLocation(program.id, name) }
			if(result == -1)
				throw IllegalArgumentException("Uniform not found: \"$name\"")
			result
		}
	}

	operator fun set(name: String, value: Float) = glCall { glUniform1f(this[name], value) }
	operator fun set(name: String, value: Int) = glCall { glUniform1i(this[name], value) }
	operator fun set(name: String, value: Vector2f) = glCall { glUniform2f(this[name], value.x, value.y) }
	operator fun set(name: String, value: Vector3f) = glCall { glUniform3f(this[name], value.x, value.y, value.z) }
	operator fun set(name: String, value: Vector4f) = glCall { glUniform4f(this[name], value.x, value.y, value.z, value.w) }
	operator fun set(name: String, value: Matrix4f) = glCall { glUniformMatrix4fv(this[name], false, value.toFloatBuffer()) }
}