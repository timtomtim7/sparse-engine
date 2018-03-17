package blue.sparse.engine.util

import blue.sparse.engine.math.*
import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.bytes.*
import blue.sparse.math.vectors.floats.*
import blue.sparse.math.vectors.ints.*
import blue.sparse.math.vectors.shorts.*

object Size
{
	private val sizes = HashMap<Class<*>, Int>()

	init
	{
		register<Byte>(1)
		register<UByte>(1)

		register<Short>(2)
		register<UShort>(2)

		register<Int>(4)
		register<UInt>(4)

		register<Float>(4)
		register<Double>(8)

		register<Vector2b>(2 * of<Byte>())
		register<Vector3b>(3 * of<Byte>())
		register<Vector4b>(4 * of<Byte>())

		register<Vector2s>(2 * of<Short>())
		register<Vector3s>(3 * of<Short>())
		register<Vector4s>(4 * of<Short>())

		register<Vector2i>(2 * of<Int>())
		register<Vector3i>(3 * of<Int>())
		register<Vector4i>(4 * of<Int>())

		register<Vector2f>(2 * of<Float>())
		register<Vector3f>(3 * of<Float>())
		register<Vector4f>(4 * of<Float>())

		register<Matrix4f>(16 * of<Float>())
	}

	operator fun get(clazz: Class<*>) = of(clazz)

	inline fun <reified T> of() = of(T::class.java)

	inline fun <reified T> register(size: Int) = register(T::class.java, size)

	fun of(clazz: Class<*>): Int
	{
		return sizes[clazz] ?: throw IllegalStateException("Size not registered for class ${clazz.name}")
	}

	fun register(clazz: Class<*>, size: Int)
	{
		if(clazz in sizes) throw IllegalStateException("Size already registered for class ${clazz.name}")
		if(size <= 0) throw IllegalArgumentException("Size cannot be <= 0")

		sizes[clazz] = size
	}
}