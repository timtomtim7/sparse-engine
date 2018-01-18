package blue.sparse.engine.asset.list

import blue.sparse.engine.asset.Asset

interface AssetList
{
	operator fun get(name: String): Asset
}