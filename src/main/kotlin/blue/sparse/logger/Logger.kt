package blue.sparse.logger

import blue.sparse.ansi.ANSI
import java.io.PrintStream

class Logger(var out: PrintStream = System.out, var level: Level = Level.DEBUG)
{
//	var useANSI = true

//	private val timeString: String
//		get()
//		{
//			return ""
//			val time = LocalTime.now()
//			return String.format("%s%02d%s:%s%02d%s:%s%02d", ANSI.Text.GRAY, time.hour, ANSI.Text.RESET, ANSI.Text.GRAY, time.minute, ANSI.Text.RESET, ANSI.Text.GRAY, time.second)
//			return "${ANSI.Text.GRAY}${time.hour}${ANSI.Text.RESET}:${ANSI.Text.GRAY}${time.minute}${ANSI.Text.RESET}:${ANSI.Text.GRAY}${time.second}"
//		}

	fun log(level: Level, message: String)
	{
//		out.println(message)
		if (level >= this.level)
			out.println("${level.formatted} ${ANSI.Text.RESET}$message")
//			out.println("${ANSI.Text.WHITE}<${ANSI.Text.GRAY}$timeString${ANSI.Text.WHITE}> ${level.formatted} ${ANSI.Text.RESET}$message")
	}

	fun log(level: Level, message: Any?) = log(level, message.toString())

	fun debug(message: String) = log(Level.DEBUG, message)
	fun info(message: String) = log(Level.INFO, message)
	fun warn(message: String) = log(Level.WARN, message)
	fun fatal(message: String) = log(Level.FATAL, message)

	fun debug(message: Any? = null) = log(Level.DEBUG, message)
	fun info(message: Any? = null) = log(Level.INFO, message)
	fun warn(message: Any? = null) = log(Level.WARN, message)
	fun fatal(message: Any? = null) = log(Level.FATAL, message)

	companion object
	{
		val default by lazy { Logger() }

		fun debug(message: String) = default.log(Level.DEBUG, message)
		fun info(message: String) = default.log(Level.INFO, message)
		fun warn(message: String) = default.log(Level.WARN, message)
		fun fatal(message: String) = default.log(Level.FATAL, message)

		fun debug(message: Any? = null) = default.log(Level.DEBUG, message)
		fun info(message: Any? = null) = default.log(Level.INFO, message)
		fun warn(message: Any? = null) = default.log(Level.WARN, message)
		fun fatal(message: Any? = null) = default.log(Level.FATAL, message)
	}
}