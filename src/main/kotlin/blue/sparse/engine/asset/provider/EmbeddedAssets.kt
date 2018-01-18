package blue.sparse.engine.asset.provider

import blue.sparse.engine.asset.Asset
import java.io.InputStream

object EmbeddedAssets : AssetProvider
{
	override fun get(name: String): Asset
	{
		return EmbeddedAsset(name)
	}

	class EmbeddedAsset internal constructor(override val path: String): Asset
	{
		override val inputStream: InputStream
			get() = ClassLoader.getSystemResourceAsStream(path)

		override val exists: Boolean
			get() = ClassLoader.getSystemResourceAsStream(path) != null
	}
}