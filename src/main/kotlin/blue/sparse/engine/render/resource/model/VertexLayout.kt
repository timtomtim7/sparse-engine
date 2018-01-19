package blue.sparse.engine.render.resource.model

import blue.sparse.engine.util.Size

class VertexLayout
{
	private val classes = ArrayList<Class<*>>()

	var size: Int = 0
		private set

	val count: Int
		get() = classes.size

	val layout: List<Class<*>>
		get() = classes.toList()

	inline fun <reified T> add()
	{
		add(T::class.java)
	}

	fun add(clazz: Class<*>)
	{
		size += Size.of(clazz)
		classes.add(clazz)
	}
}