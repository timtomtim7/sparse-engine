package blue.sparse.engine.render.resource

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.util.ColorFormat
import blue.sparse.extensions.toByteBuffer
import blue.sparse.math.vectors.ints.Vector2i
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture

class Texture(asset: Asset) : Resource(), Bindable
{
	internal val id: Int
	val size: Vector2i
		get() = field.clone()

	init
	{
		id = glCall { glGenTextures() }

		val image = asset.readImage()
		size = Vector2i(image.width, image.height)
		bind {
			glCall { glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.toByteBuffer(ColorFormat.RGBA)) }
			glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT) }
			glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT) }
			glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
			glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
		}
	}

	fun bind(id: Int)
	{
		glCall { glActiveTexture(GL_TEXTURE0 + id) }
		bind()
	}

	inline fun <R> bind(id: Int, body: Texture.() -> R): R
	{
		bind(id)
		val result = body()
		unbind(id)
		return result
	}

	override fun bind()
	{
		glCall { glBindTexture(GL_TEXTURE_2D, id) }
	}

	fun unbind(id: Int)
	{
		glCall { glActiveTexture(GL_TEXTURE0 + id) }
		unbind()
		glCall { glActiveTexture(GL_TEXTURE0) }
	}

	override fun unbind()
	{
		glCall { glBindTexture(GL_TEXTURE_2D, 0) }
	}

	override fun release()
	{
		glCall { glDeleteTextures(id) }
	}
}