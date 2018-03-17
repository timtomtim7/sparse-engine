package blue.sparse.extensions

import blue.sparse.engine.util.ColorFormat
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun ByteBuffer.toBufferedImage(width: Int, height: Int, format: ColorFormat = ColorFormat.RGBA): BufferedImage {
	val hasAlpha = format.hasAlpha
	val image = BufferedImage(width, height, if (hasAlpha) BufferedImage.TYPE_INT_ARGB else BufferedImage.TYPE_INT_RGB)
//	println(hasAlpha)

	for (y in 0 until height) {
		for (x in 0 until width) {
			var pixel = 0x00000000

			format.order.forEach {
				val byte = get().toInt() and 0xFF
//				println(byte)
				pixel = when(it) {
					0 -> pixel or (byte shl 16)
					1 -> pixel or (byte shl 8)
					2 -> pixel or (byte)
					3 -> pixel or if(hasAlpha) (byte shl 24) else (0xFF shl 24)
					else -> pixel
				}
			}

//			println("#"+(pixel.toLong() and 0xFFFFFFFF).toString(16))

			image.setRGB(x, y, pixel)
		}
	}

	return image
}

fun BufferedImage.toByteBuffer(format: ColorFormat = ColorFormat.RGBA): ByteBuffer {
	val pixels = getRGB(0, 0, width, height, null, 0, width)

	val buffer = ByteBuffer.allocateDirect(height * width * format.order.size).order(ByteOrder.nativeOrder())
	val hasAlpha = colorModel.hasAlpha()

//	for (y in height-1 downTo 0)
	for (y in 0 until height) {
		for (x in 0 until width) {
			val pixel = pixels[y * width + x]

			format.order.forEach {
				when (it) {
					0 -> buffer.put((pixel shr 16 and 0xFF).toByte())
					1 -> buffer.put((pixel shr 8 and 0xFF).toByte())
					2 -> buffer.put((pixel and 0xFF).toByte())
					3 -> if (hasAlpha) buffer.put((pixel shr 24 and 0xFF).toByte()) else buffer.put(0xFF.toByte())
				}
			}
		}
	}

	buffer.flip()
	return buffer
}