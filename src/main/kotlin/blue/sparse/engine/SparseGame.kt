package blue.sparse.engine

abstract class SparseGame
{
	val engine = SparseEngine
	inline val window get() = engine.window
	inline val input get() = window.input

	open fun update(delta: Float) {}
	open fun render(delta: Float) {}
}