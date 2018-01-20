package blue.sparse.engine.asset.model

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.model.Model

interface ModelLoader
{
	fun isExtensionSupported(extension: String): Boolean

	fun load(asset: Asset): Model
}