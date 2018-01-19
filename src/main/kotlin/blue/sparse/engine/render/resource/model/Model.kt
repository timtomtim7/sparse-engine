package blue.sparse.engine.render.resource.model

import blue.sparse.engine.render.resource.Bindable

interface Model : Bindable
{
	fun render()

	override fun bind()
	override fun unbind()
}