package blue.sparse.engine.asset.model

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.model.*
import java.io.BufferedReader
import java.io.InputStream

object PolygonModelLoader: ModelLoader
{
	override fun isExtensionSupported(extension: String): Boolean
	{
		return extension == "ply"
	}

	override fun load(asset: Asset): Model
	{
		val inp = asset.inputStream
		val reader = inp.bufferedReader()

		if(reader.readLine() != "ply")
		{
			reader.close()
			throw IllegalArgumentException("PLY asset has invalid header")
		}

		val format = reader.readLine().split(" ")

		return when(format[1])
		{
			"ascii" -> ASCIIFormat.load(reader)
			"binary", "binary_little_endian", "binary_big_endian" -> BinaryFormat.load(inp)
			else -> throw UnsupportedOperationException("PLY file specifies unsupported format \"${format[1]}\"")
		}
	}

	object ASCIIFormat
	{
		fun load(reader: BufferedReader): Model
		{
			val header = reader.lineSequence().takeWhile { it != "end_header" }.filter { !it.startsWith("comment") }.toList()


			return BasicModel(VertexArray())
		}
	}

	object BinaryFormat
	{
		fun load(stream: InputStream): Model
		{
			return BasicModel(VertexArray())
		}
	}

	private enum class DataTypes(val size: Int)
	{
		CHAR(1),
		UCHAR(1),
		SHORT(2),
		USHORT(2),
		INT(4),
		UINT(4),
		FLOAT(4),
		DOUBLE(8)
	}
}