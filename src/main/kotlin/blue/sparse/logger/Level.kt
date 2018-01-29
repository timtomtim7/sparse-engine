package blue.sparse.logger

import blue.sparse.ansi.ANSI

enum class Level(val color: ANSI.Text): Comparable<Level>
{
	DEBUG(ANSI.Text.BRIGHT_YELLOW),
	INFO(ANSI.Text.BLUE),
	WARN(ANSI.Text.RED),
	FATAL(ANSI.Text.BRIGHT_RED);

	val formatted = String.format("%-21s", "${ANSI.Text.RESET}[$color$name${ANSI.Text.RESET}]")
	val formattedNoColor = String.format("%-5s", name)
}