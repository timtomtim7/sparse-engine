package blue.sparse.engine.asset

import blue.sparse.engine.asset.provider.*
import java.io.File

object AssetManager
{
	private val providers = ArrayList<AssetProvider>()

	init
	{
		registerProvider(EmbeddedAssets)
		registerProvider(AssetDirectory(File("assets").absoluteFile))
	}

	fun registerProvider(provider: AssetProvider)
	{
		if(provider in providers) throw IllegalArgumentException("Provider already registered.")
		providers.add(provider)
	}

	operator fun get(path: String): List<Asset>
	{
		return providers.mapNotNull { it[path].takeIf { it.exists } }
	}

	fun getLast(path: String): Asset?
	{
		return get(path).lastOrNull()
	}
}