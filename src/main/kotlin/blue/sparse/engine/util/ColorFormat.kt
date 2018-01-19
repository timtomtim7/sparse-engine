package blue.sparse.engine.util

class ColorFormat(vararg order: Int)
{
	init
	{
		if(order.any { it !in 0..3 })
			throw IllegalArgumentException("All values in `order` must be in the range 0..3")
	}

	val order: IntArray = order
		get() = field.clone()

	override fun equals(other: Any?): Boolean
	{
		if (this === other) return true
		if (other !is ColorFormat) return false
		return true
	}

	override fun hashCode(): Int
	{
		return javaClass.hashCode()
	}

	companion object
	{
		val RGB = ColorFormat(0, 1, 2)
		val RGBA = ColorFormat(0, 1, 2, 3)
		val ARGB = ColorFormat(3, 0, 1, 2)
		val BGR = ColorFormat(2, 1, 0)
		val R = ColorFormat(0)
		val G = ColorFormat(1)
		val B = ColorFormat(2)
	}
}