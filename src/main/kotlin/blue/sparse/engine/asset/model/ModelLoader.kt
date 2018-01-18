package blue.sparse.engine.asset.model

import blue.sparse.engine.render.resource.model.Model
import java.io.InputStream

interface ModelLoader
{
	fun isExtensionSupported(extension: String): Boolean

	fun load(input: InputStream): Model
}