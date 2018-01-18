package blue.sparse.engine.render.resource

abstract class Resource
{
	init
	{
		resources.add(this)
	}

	protected abstract fun release()

	fun delete()
	{
		release()
		resources.remove(this)
	}

	companion object
	{
		private val resources = HashSet<Resource>()

		internal fun deleteAll()
		{
			resources.forEach(Resource::delete)
		}
	}
}
