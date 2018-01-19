package blue.sparse.engine.window.input

interface Button
{
	val id: Int
	val type: Type

	enum class Type
	{
		KEY, MOUSE, CONTROLLER
	}
}