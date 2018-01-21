package blue.sparse.ansi

object ANSI
{
	private const val ESCAPE = "\u001b["

	enum class Text(code: String)
	{
		BLACK("30m"),
		RED("31m"),
		GREEN("32m"),
		YELLOW("33m"),
		BLUE("34m"),
		PURPLE("35m"),
		CYAN("36m"),
		GRAY("37m"),

		DARK_GREY("90m"),
		BRIGHT_RED("91m"),
		BRIGHT_GREEN("92m"),
		BRIGHT_YELLOW("93m"),
		BRIGHT_BLUE("94m"),
		BRIGHT_MAGENTA("95m"),
		BRIGHT_CYAN("96m"),
		WHITE("97m"),

		RESET("0m");

		val code = "$ESCAPE$code"

		override fun toString() = code
	}

	enum class Background(code: String)
	{
		BLACK("40m"),
		RED("41m"),
		GREEN("42m"),
		YELLOW("43m"),
		BLUE("44m"),
		PURPLE("45m"),
		CYAN("46m"),
		GRAY("47m"),

		DARK_GREY("100m"),
		BRIGHT_RED("101m"),
		BRIGHT_GREEN("102m"),
		BRIGHT_YELLOW("103m"),
		BRIGHT_BLUE("104m"),
		BRIGHT_MAGENTA("105m"),
		BRIGHT_CYAN("106m"),
		WHITE("107m"),

		RESET("0m");

		val code = "$ESCAPE$code"

		override fun toString() = code
	}

	enum class Decoration(code: String)
	{
		BOLD("1m"),
		UNDERLINE("4m"),
		INVERTED("7m"),
		RESET("0m");

		val code = "$ESCAPE$code"

		override fun toString() = code
	}
}