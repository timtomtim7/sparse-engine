package blue.sparse.engine.util

import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.bytes.*
import blue.sparse.math.vectors.floats.*
import blue.sparse.math.vectors.ints.*
import blue.sparse.math.vectors.shorts.*
import java.nio.ByteBuffer

interface Serializer<T: Any>
{
	fun put(buffer: ByteBuffer, value: T)
	fun get(buffer: ByteBuffer): T

	//AHHHHHHHHHHH KILL ME (?)
	companion object
	{
		private val registered = HashMap<Class<*>, Serializer<*>>()

		init
		{
			register({ put(it) }, ByteBuffer::get)
			register({ putShort(it) }, ByteBuffer::getShort)
			register({ putInt(it) }, ByteBuffer::getInt)
			register({ putFloat(it) }, ByteBuffer::getFloat)

//			register({ put(it.byte) }, { UByte(get()) })
//			register({ putShort(it.short) }, { UShort(short) } )
//			register({ putInt(it.int) }, { UInt(int) })

			register({ putFloat(it.x).putFloat(it.y) }, { Vector2f(float, float) })
			register({ putFloat(it.x).putFloat(it.y).putFloat(it.z) }, { Vector3f(float, float, float) })
			register({ putFloat(it.x).putFloat(it.y).putFloat(it.z).putFloat(it.w) }, { Vector4f(float, float, float, float) })

			register({ putInt(it.x).putInt(it.y) }, { Vector2i(int, int) })
			register({ putInt(it.x).putInt(it.y).putInt(it.z) }, { Vector3i(int, int, int) })
			register({ putInt(it.x).putInt(it.y).putInt(it.z).putInt(it.w) }, { Vector4i(int, int, int, int) })

			register({ putShort(it.x).putShort(it.y) }, { Vector2s(short, short) })
			register({ putShort(it.x).putShort(it.y).putShort(it.z) }, { Vector3s(short, short, short) })
			register({ putShort(it.x).putShort(it.y).putShort(it.z).putShort(it.w) }, { Vector4s(short, short, short, short) })

			register({ put(it.x).put(it.y) }, { Vector2b(get(), get()) })
			register({ put(it.x).put(it.y).put(it.z) }, { Vector3b(get(), get(), get()) })
			register({ put(it.x).put(it.y).put(it.z).put(it.w) }, { Vector4b(get(), get(), get(), get()) })

			register(
					{ put(it.toByteBuffer()) },
					{ Matrix4f.fromByteBuffer(this) }
			)
		}

		inline fun <reified T: Any> register(serializer: Serializer<T>) = register(T::class.java, serializer)

		inline fun <reified T: Any> register(crossinline put: ByteBuffer.(T) -> Unit, crossinline get: ByteBuffer.() -> T)
		{
			register(T::class.java, object : Serializer<T>
			{
				override fun put(buffer: ByteBuffer, value: T) = put(buffer, value)
				override fun get(buffer: ByteBuffer) = get(buffer)
			})
		}

		inline fun <reified T: Any> get() = get(T::class.java)
		inline fun <reified T: Any> serialize(target: ByteBuffer, value: T) = get<T>().put(target, value)

		fun <T: Any> register(clazz: Class<T>, serializer: Serializer<T>)
		{
			if (clazz in registered) throw IllegalStateException("${clazz.name} already registered")
			registered[clazz] = serializer
		}

		@Suppress("UNCHECKED_CAST")
		fun <T: Any> get(clazz: Class<T>): Serializer<T>
		{
			val serializer = registered[clazz] ?: throw IllegalStateException("Serializer for ${clazz.name} not registered")
			return serializer as Serializer<T>
		}
	}
}