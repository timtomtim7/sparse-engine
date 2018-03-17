package blue.sparse.engine.render.resource

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.util.ColorFormat
import blue.sparse.extensions.toBufferedImage
import blue.sparse.extensions.toByteBuffer
import blue.sparse.math.vectors.ints.Vector2i
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE
import org.lwjgl.opengl.GL13.GL_TEXTURE0
import org.lwjgl.opengl.GL13.glActiveTexture
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Texture(image: BufferedImage) : Resource(), Bindable {
	internal val id: Int
	val size: Vector2i
		get() = field.clone()

	init {
		id = glCall { glGenTextures() }

		size = Vector2i(image.width, image.height)
		bind {
			glCall { glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.width, image.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, image.toByteBuffer(ColorFormat.RGBA)) }
			repeat()
			linearFiltering()
		}
	}

	fun clampToEdge() = bind {
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE) }
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE) }
	}

	fun repeat() = bind {
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT) }
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT) }
	}

	fun linearFiltering() = bind {
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR) }
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR) }
	}

	fun nearestFiltering() = bind {
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST) }
		glCall { glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST) }
	}

	constructor(asset: Asset) : this(asset.readImage())

	fun read(): BufferedImage {
		val buffer = ByteBuffer
				.allocateDirect(size.x * size.y * ColorFormat.RGBA.order.size)
				.order(ByteOrder.nativeOrder())

		bind {
			glCall { glGetTexImage(GL_TEXTURE_2D, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer) }
		}

		return buffer.toBufferedImage(size.x, size.y)
	}

	fun subImage(asset: Asset, origin: Vector2i) {
		subImage(asset.readImage(), origin)
	}

	fun subImage(image: BufferedImage, origin: Vector2i) {
		bind {
			glCall { glTexSubImage2D(GL_TEXTURE_2D, 0, origin.x, origin.y, image.width, image.height, GL_RGBA, GL_UNSIGNED_BYTE, image.toByteBuffer(ColorFormat.RGBA)) }
		}
	}

	fun fillSection(min: Vector2i, max: Vector2i, rgba: Int) {
		val size = max - min
		val area = size.x * size.y

		val buffer = ByteBuffer
				.allocateDirect(area * ColorFormat.RGBA.order.size)
				.order(ByteOrder.nativeOrder())

		for(i in 1..area) {
			buffer.putInt(rgba)
		}
		buffer.flip()

		println("$min $max $size $area")

		bind {
			glCall { glTexSubImage2D(GL_TEXTURE_2D, 0, min.x, min.y, size.x, size.y, GL_RGBA, GL_UNSIGNED_BYTE, buffer) }
		}
	}

	fun bind(id: Int) {
		glCall { glActiveTexture(GL_TEXTURE0 + id) }
		bind()
	}

	inline fun <R> bind(id: Int, body: Texture.() -> R): R {
		bind(id)
		val result = body()
		unbind(id)
		return result
	}

	override fun bind() {
		glCall { glBindTexture(GL_TEXTURE_2D, id) }
	}

	fun unbind(id: Int) {
		glCall { glActiveTexture(GL_TEXTURE0 + id) }
		unbind()
		glCall { glActiveTexture(GL_TEXTURE0) }
	}

	override fun unbind() {
		glCall { glBindTexture(GL_TEXTURE_2D, 0) }
	}

	override fun release() {
		glCall { glDeleteTextures(id) }
	}
}