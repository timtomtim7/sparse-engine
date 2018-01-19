package blue.sparse.engine.asset

import blue.sparse.engine.asset.provider.AssetProvider
import blue.sparse.extensions.readText
import java.io.InputStream
import javax.imageio.ImageIO

interface Asset
{
	val path: String
	val inputStream: InputStream

	val exists: Boolean

	fun readBytes() = inputStream.readBytes()
	fun readText() = inputStream.readText()
	fun readImage() = ImageIO.read(inputStream)

	companion object: AssetProvider
	{
		override fun get(path: String) = AssetManager[path]
	}
}