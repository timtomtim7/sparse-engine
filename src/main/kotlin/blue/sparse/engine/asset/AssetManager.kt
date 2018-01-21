package blue.sparse.engine.asset

import blue.sparse.engine.asset.provider.*
import blue.sparse.engine.errors.AssetNotFoundException
import blue.sparse.logger.Logger
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
		return optional(path) ?: throw AssetNotFoundException(path)
	}

	fun optional(path: String): Asset?
	{
		val all = getAll(path)
		if(all.isEmpty())
		{
			Logger.warn("Asset not found \"$path\"")
			return null
		}
		Logger.info("Retrieving asset \"$path\"")
		return all.last()
	}

	fun getAll(path: String): List<Asset>
	{
		return providers.mapNotNull { it[path] }
	}
}