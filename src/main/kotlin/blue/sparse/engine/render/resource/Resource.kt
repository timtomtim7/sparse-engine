package blue.sparse.engine.render.resource

import java.util.concurrent.ConcurrentSkipListSet

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
		private val resources = ConcurrentSkipListSet<Resource>()

		internal fun deleteAll()
		{
			resources.forEach(Resource::delete)
		}
	}
}
