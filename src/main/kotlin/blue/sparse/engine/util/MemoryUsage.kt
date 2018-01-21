package blue.sparse.engine.util

object MemoryUsage
{
	fun getMemoryUsedString(): String
	{
		val rt = Runtime.getRuntime()
		val freeMemory = rt.freeMemory()
		val totalMemory = rt.totalMemory()
//		val maxMemory = rt.maxMemory()

		val used = totalMemory - freeMemory

		return formatByteCount(used)
	}

	fun formatByteCount(bytes: Long): String
	{
		val units = arrayOf("Bi", "KiB", "MiB", "GiB")
		val exponent = (Math.log10(bytes.toDouble()) / Math.log10(1024.0)).toInt().toDouble()
		val pow = Math.pow(1024.0, exponent)

		return String.format("%.2f %s", bytes.toDouble() / pow, units[exponent.toInt()])
	}
}