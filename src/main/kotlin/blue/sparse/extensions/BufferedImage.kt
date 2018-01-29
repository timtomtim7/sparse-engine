package blue.sparse.extensions

import blue.sparse.engine.util.ColorFormat
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun BufferedImage.toByteBuffer(format: ColorFormat = ColorFormat.RGBA): ByteBuffer
{
	val pixels = getRGB(0, 0, width, height, null, 0, width)

	val buffer = ByteBuffer.allocateDirect(height * width * format.order.size).order(ByteOrder.nativeOrder())
	val hasAlpha = colorModel.hasAlpha()

//	for (y in height-1 downTo 0)
	for (y in 0 until height)
	{
		for (x in 0 until width)
		{
			val pixel = pixels[y * width + x]

			format.order.forEach {
				when (it)
				{
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