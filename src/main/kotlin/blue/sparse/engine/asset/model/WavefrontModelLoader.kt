package blue.sparse.engine.asset.model

import blue.sparse.engine.render.resource.model.Model
import java.io.InputStream

object WavefrontModelLoader: ModelLoader
{
	override fun isExtensionSupported(extension: String): Boolean
	{
		return extension == "obj"
	}

	override fun load(input: InputStream): Model
	{
		TODO("not implemented")
	}
}