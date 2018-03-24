package blue.sparse.engine.render.resource

import java.util.WeakHashMap

abstract class Resource {
	private var deleted: Boolean = false

	init {
		resources[this] = Unit
	}

	protected abstract fun release()

	fun delete() {
		if (deleted) return
		release()

		resources.remove(this)
		deleted = true
	}

	protected fun finalize() {
		//TODO: This was trying to access OpenGL from another thread.
//		delete()
	}

	companion object {
		private val resources = WeakHashMap<Resource, Unit>()

		internal fun deleteAll() {
			resources.keys.toList().forEach(Resource::delete)
			resources.clear()
		}
	}
}
