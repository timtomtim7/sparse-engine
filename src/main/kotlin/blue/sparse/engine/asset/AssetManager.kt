package blue.sparse.engine.asset

import blue.sparse.engine.asset.provider.*
import java.io.File

object AssetManager: AssetProvider
{
	private val providers = ArrayList<AssetProvider>()

	init
	{
		registerProvider(EmbeddedAssets)
		registerProvider(AssetDirectory(File("assets").absoluteFile))
	}

	fun registerProvider(provider: AssetProvider)
	{
		if(provider == this) return
		if(provider in providers) throw IllegalArgumentException("Provider already registered.")

		providers.add(provider)
	}

	override operator fun get(path: String): Asset
	{
		return getAll(path).last()
	}

	fun getAll(path: String): List<Asset>
	{
		return providers.mapNotNull { it[path] }
	}
}