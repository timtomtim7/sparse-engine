package blue.sparse.engine.render.resource.model

class VertexLayout
{
	private val classes = ArrayList<Class<*>>()
	private var size: Int = 0

	inline fun <reified T> add()
	{
		add(T::class.java)
	}

	fun add(clazz: Class<*>)
	{
		classes.add(clazz)
	}
}