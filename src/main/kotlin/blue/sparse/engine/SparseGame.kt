package blue.sparse.engine

abstract class SparseGame
{
	abstract fun init(engine: SparseEngine)
	abstract fun update(delta: Float)
	abstract fun render(delta: Float)
}