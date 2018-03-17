package blue.sparse.engine.asset.provider

import blue.sparse.engine.asset.Asset
import java.io.*
import java.util.zip.ZipFile

class AssetArchive(val archive: File, val prefix: String = "") : AssetProvider
{
	private val zip: ZipFile

	init
	{
		if (!archive.exists())
			throw FileNotFoundException(archive.absolutePath)

		zip = ZipFile(archive)
	}

	override fun close()
	{
		zip.close()
	}

	override fun get(path: String): Asset?
	{
		if(zip.getEntry(prefix+path) == null) return null
		return ZipAsset(prefix+path)
	}

	inner class ZipAsset internal constructor(override val path: String) : Asset
	{
		override val inputStream: InputStream
			get() = zip.getInputStream(zip.getEntry(path))

		override val exists: Boolean
			get() = zip.getEntry(path) != null

	}
}