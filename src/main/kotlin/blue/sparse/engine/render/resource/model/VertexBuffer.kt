package blue.sparse.engine.render.resource.model

import blue.sparse.engine.util.Serializer
import blue.sparse.engine.util.Size
import java.nio.ByteBuffer
import java.nio.ByteOrder

class VertexBuffer
{
	private val data = ArrayList<Any>()
	var size: Int = 0
		private set

	fun add(value: Any)
	{
		Serializer.get(value.javaClass)
		size += Size.of(value.javaClass)
		data.add(value)
	}

	fun add(vararg values: Any)
	{
		values.forEach(this::add)
	}

	fun addAll(values: Iterable<Any>)
	{
		values.forEach(this::add)
	}

	fun zipAdd(vararg values: Collection<Any>)
	{
		if(values.isEmpty()) return
		if(values.any { it.size != values.first().size })
			throw IllegalArgumentException("Collections must all have the same size.")

		val iterators = values.map(Iterable<Any>::iterator)
		while(iterators.first().hasNext())
		{
			iterators.forEach {
				add(it.next())
			}
		}
	}

	fun checkLayout(layout: VertexLayout): Boolean
	{
		val classes = layout.layout

		if(data.size % classes.size != 0)
			return false

		var remaining = classes.iterator()

		for(value in data)
		{
			if(!remaining.hasNext())
				remaining = classes.iterator()

			if(!remaining.next().isInstance(value))
				return false
		}

		return true
	}

	fun toByteBuffer(): ByteBuffer
	{
		val byteBuffer: ByteBuffer = ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder())

		for (value in data)
		{
			val serializer = Serializer.get(value.javaClass)
			serializer.put(byteBuffer, value)
		}

		byteBuffer.flip()
		return byteBuffer
	}
}