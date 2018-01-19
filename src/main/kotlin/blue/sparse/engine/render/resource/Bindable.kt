package blue.sparse.engine.render.resource

interface Bindable
{
	fun bind()
	fun unbind()
}

inline fun <T: Bindable, R> T.bind(body: T.() -> R): R
{
	bind()
	val result = run(body)
	unbind()
	return result
}