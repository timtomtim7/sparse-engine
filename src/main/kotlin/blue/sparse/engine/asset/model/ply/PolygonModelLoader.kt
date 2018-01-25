package blue.sparse.engine.asset.model.ply

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.ModelLoader
import blue.sparse.engine.render.resource.model.EmptyModel
import blue.sparse.engine.render.resource.model.Model
import java.io.BufferedReader
import java.io.InputStream
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible

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

		if (reader.readLine() != "ply")
		{
			reader.close()
			throw IllegalArgumentException("PLY asset has invalid header")
		}

		val format = reader.readLine().split(" ")

		return when (format[1])
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
			val elementDefs = parseHeader(reader)

			println(elementDefs)

			for(def in elementDefs)
			{
				for(i in 1..def.count)
				{
					val line = reader.readLine().split(" ").listIterator()
					println("{")
					def.properties.map {
						val value = it.second.parse(line)

						println("\t${it.first} = $value")
					}
					println("}")
				}
			}

			return EmptyModel
		}

		private fun parseHeader(reader: BufferedReader): List<ElementDef>
		{
			val header = reader
					.lineSequence()
					.takeWhile { it != "end_header" }
					.filter { !it.startsWith("comment") }
					.map { it.split(" ") }
					.toList()

			val elements = ArrayList<ElementDef>()
			var element: ElementDef? = null

			for (line in header)
			{
				when (line[0])
				{
					"element" ->
					{
						element = ElementDef(line[1], line[2].toInt())
						elements.add(element)
					}
					"property" ->
					{
						val baseType = DataType.valueOf(line[1].toUpperCase())
						val name: String
						val type: Type<*>
						if (baseType == DataType.LIST)
						{
							val countType = DataType.valueOf(line[2].toUpperCase()).construct()
							val valueType = DataType.valueOf(line[3].toUpperCase()).construct()
							name = line[4]

							type = baseType.constructor.call(countType, valueType)
						} else
						{
							name = line[2]
							type = baseType.construct()
						}

						element!!.properties.add(name to type)
					}
				}
			}

			return elements
		}
	}

	object BinaryFormat
	{
		fun load(stream: InputStream): Model
		{
			return EmptyModel
		}
	}

	private data class Element(val name: String, val properties: Map<String, *>)

	private data class ElementDef(val name: String, val count: Int, val properties: MutableList<Pair<String, Type<*>>> = ArrayList())

	private enum class DataType(val size: Int, clazz: KClass<out Type<*>>)
	{
		CHAR	(1, IntegerType.CharType::class),
		UCHAR	(1, IntegerType.UnsignedCharType::class),
		SHORT	(2, IntegerType.ShortType::class),
		USHORT	(2, IntegerType.UnsignedShortType::class),
		INT		(4, IntegerType.IntType::class),
		UINT	(4, IntegerType.UnsignedIntType::class),
		FLOAT	(4, DecimalType.FloatType::class),
		DOUBLE	(8, DecimalType.DoubleType::class),
		LIST	(0, ListType::class);

		val constructor = clazz.primaryConstructor!!.apply {
			isAccessible = true
		}

		fun construct(): Type<*>
		{
			return constructor.call()
		}
	}

	private abstract class Type<T>
	{
		abstract val type: DataType

		abstract fun parse(tokens: ListIterator<String>): T

		override fun toString(): String
		{
			return "Type($type)"
		}
	}

	private sealed class DecimalType<T: Number>(override val type: DataType): Type<T>()
	{
		class FloatType: DecimalType<Float>(DataType.FLOAT)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toFloat()
		}

		class DoubleType: DecimalType<Double>(DataType.DOUBLE)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toDouble()
		}
	}

	private sealed class IntegerType<T: Number>(override val type: DataType): Type<T>()
	{
		class CharType : IntegerType<Byte>(DataType.CHAR)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toInt().toByte()
		}

		class UnsignedCharType : IntegerType<Byte>(DataType.UCHAR)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toInt().toByte()
		}

		class ShortType : IntegerType<Short>(DataType.SHORT)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toShort()
		}

		class UnsignedShortType : IntegerType<Short>(DataType.USHORT)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toShort()
		}

		class IntType : IntegerType<Int>(DataType.INT)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toInt()
		}

		class UnsignedIntType : IntegerType<Int>(DataType.UINT)
		{
			override fun parse(tokens: ListIterator<String>) = tokens.next().toInt()
		}
	}

	private class ListType<T>(val countType: IntegerType<*>, val valueType: Type<T>): Type<List<T>>()
	{
		override val type = DataType.LIST

		override fun parse(tokens: ListIterator<String>): List<T>
		{
			val count = countType.parse(tokens).toInt()
			val result = ArrayList<T>()

			for(i in 1..count)
				result.add(valueType.parse(tokens))

			return result
		}
	}
}