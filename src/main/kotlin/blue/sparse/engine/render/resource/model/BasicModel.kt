package blue.sparse.engine.render.resource.model

class BasicModel(val array: VertexArray) : Model
{
	override fun render()
	{
		array.render()
	}
}