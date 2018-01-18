package blue.sparse.engine.asset.provider

import blue.sparse.engine.asset.Asset
import java.io.File
import java.io.InputStream

class AssetDirectory(val directory: File): AssetProvider
{
	init
	{
		if(!directory.exists() || !directory.isDirectory)
			throw IllegalArgumentException("File either does not exist or is not a directory.")
	}

	override fun get(name: String): Asset
	{
		val file = File(directory, name)
		return FileAsset(file, name)
	}

	class FileAsset internal constructor(val file: File, override val path: String): Asset
	{
		override val inputStream: InputStream
			get() = file.inputStream()

		override val exists: Boolean
			get() = file.exists() && !file.isDirectory

	}
}