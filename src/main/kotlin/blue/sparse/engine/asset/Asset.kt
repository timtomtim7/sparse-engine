package blue.sparse.engine.asset

import java.io.InputStream

interface Asset
{
	val path: String
	val inputStream: InputStream

	val exists: Boolean
}