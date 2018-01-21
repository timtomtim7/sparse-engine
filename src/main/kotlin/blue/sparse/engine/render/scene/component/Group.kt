package blue.sparse.engine.render.scene.component

open class Group : Transformed(), MutableSet<Component>
{
	private val components = LinkedHashSet<Component>()

	override fun add(element: Component): Boolean
	{
		TODO("not implemented")
	}

	override fun addAll(elements: Collection<Component>): Boolean
	{
		TODO("not implemented")
	}

	override fun clear()
	{
		TODO("not implemented")
	}

	override fun iterator(): MutableIterator<Component>
	{
		TODO("not implemented")
	}

	override fun remove(element: Component): Boolean
	{
		TODO("not implemented")
	}

	override fun removeAll(elements: Collection<Component>): Boolean
	{
		TODO("not implemented")
	}

	override fun retainAll(elements: Collection<Component>): Boolean
	{
		TODO("not implemented")
	}

	override val size: Int
		get() = TODO("not implemented")

	override fun contains(element: Component): Boolean
	{
		TODO("not implemented")
	}

	override fun containsAll(elements: Collection<Component>): Boolean
	{
		TODO("not implemented")
	}

	override fun isEmpty(): Boolean
	{
		TODO("not implemented")
	}

}