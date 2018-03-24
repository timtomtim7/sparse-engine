package blue.sparse.engine.render

import blue.sparse.engine.errors.glCall
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import org.lwjgl.opengl.GL20.glUseProgram
import org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP
import kotlin.reflect.KProperty

object StateManager {

	private var boundShaderProgram: Int = 0
	private var boundTexture2D: IntArray = IntArray(32)

	var activeTexture: Int = 0
		set(value) {
			if(value == field)
				return
			if(value < 0 || value >= 32)
				throw IllegalArgumentException("Texture slot out of bounds.")

			glCall { glActiveTexture(GL_TEXTURE0 + value) }
			field = value
		}

	var blend by EnableState(GL_BLEND)
	var depthTest by EnableState(GL_DEPTH_TEST)
	var cullFace by EnableState(GL_CULL_FACE)
	var depthClamp by EnableState(GL_DEPTH_CLAMP)

	var depthMask: Boolean = true
		set(value) {
			if(field == value)
				return

			glCall { glDepthMask(value) }
			field = value
		}

	fun bindShaderProgram(program: Int) {
		if(boundShaderProgram == program)
			return
		glCall { glUseProgram(program) }
		boundShaderProgram = program
	}

	fun bindTexture2D(slot: Int, texture: Int) {
		if(texture == boundTexture2D[slot])
			return

		activeTexture = slot
		glCall { glBindTexture(GL_TEXTURE_2D, texture) }
		boundTexture2D[slot] = texture
	}

	internal class EnableState(val id: Int, private var enabled: Boolean = false) {

		operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Boolean) {
			enabled = value
			if (enabled) {
				glCall { glEnable(id) }
			} else {
				glCall { glDisable(id) }
			}
		}

		operator fun getValue(thisRef: Any?, property: KProperty<*>): Boolean {
			return enabled
		}
	}
}