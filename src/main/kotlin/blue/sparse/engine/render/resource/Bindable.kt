package blue.sparse.engine.render.resource

interface Bindable
{
	fun bind()
	fun unbind()
}

inline fun <T: Bindable> T.bind(body: T.() -> Unit)
{
	bind()
	apply(body)
	unbind()
}