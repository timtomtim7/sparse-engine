package blue.sparse.engine

abstract class SparseGame
{
	lateinit var engine: SparseEngine<*> private set
	inline val window get() = engine.window
	inline val input get() = window.input

	fun init(engine: SparseEngine<*>)
	{
		this.engine = engine
		init()
	}

	open fun init() {}

	open fun update(delta: Float) {}
	open fun render(delta: Float) {}
}