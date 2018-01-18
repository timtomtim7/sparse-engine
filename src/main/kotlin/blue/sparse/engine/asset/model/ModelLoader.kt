package blue.sparse.engine.asset.model

import blue.sparse.engine.render.resource.Model
import java.io.InputStream

interface ModelLoader
{
	fun load(input: InputStream): Model
}