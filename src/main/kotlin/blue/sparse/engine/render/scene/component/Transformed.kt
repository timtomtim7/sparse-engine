package blue.sparse.engine.render.scene.component

import blue.sparse.math.FloatTransform
import blue.sparse.math.matrices.Matrix4f

abstract class Transformed(val transform: FloatTransform = FloatTransform()) : Component
{
	private var transformHashCode: Int = transform.hashCode()

	var modelMatrix: Matrix4f = transform.matrix
		private set
		get()
		{
			val hashCode = transform.hashCode()
			if(hashCode != transformHashCode)
			{
				field = transform.matrix
				transformHashCode = hashCode
			}
			return field
		}
}