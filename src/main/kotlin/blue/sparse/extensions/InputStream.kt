package blue.sparse.extensions

import java.awt.image.BufferedImage
import java.io.InputStream
import javax.imageio.ImageIO

fun InputStream.readText(): String
{
	return bufferedReader().use { it.readText() }
}

fun InputStream.readImage(): BufferedImage
{
	return use(ImageIO::read)
}