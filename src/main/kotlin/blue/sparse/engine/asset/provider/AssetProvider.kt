package blue.sparse.engine.asset.provider

import blue.sparse.engine.asset.Asset

interface AssetProvider
{
	operator fun get(name: String): Asset

	fun close() {}
}