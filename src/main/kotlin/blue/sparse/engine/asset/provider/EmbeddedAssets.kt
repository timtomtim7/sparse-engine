package blue.sparse.engine.asset.provider

import blue.sparse.engine.asset.Asset
import java.io.InputStream

object EmbeddedAssets : AssetProvider
{
	override fun get(path: String): Asset?
	{
		if(ClassLoader.getSystemResource(path) == null) return null
		return EmbeddedAsset(path)
	}

	class EmbeddedAsset internal constructor(override val path: String): Asset
	{
		override val inputStream: InputStream
			get() = ClassLoader.getSystemResourceAsStream(path)

		override val exists: Boolean
			get() = ClassLoader.getSystemResourceAsStream(path) != null
	}
}