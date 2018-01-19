package blue.sparse.engine.window.input

enum class MouseButton(override val id: Int) : Button
{
	UNKNOWN(-1),
	LEFT(0),
	RIGHT(1),
	MIDDLE(2),
	M4(3),
	M5(4),
	M6(5),
	M7(6),
	;

	override val type: Button.Type = Button.Type.KEY

	companion object
	{
		private val mouseButtons = arrayOfNulls<MouseButton>(349)

		init
		{
			for (key in values())
			{
				if (key.id !in mouseButtons.indices) continue
				mouseButtons[key.id] = key
			}
		}

		operator fun get(id: Int): MouseButton
		{
			return mouseButtons[id] ?: UNKNOWN
		}
	}
}