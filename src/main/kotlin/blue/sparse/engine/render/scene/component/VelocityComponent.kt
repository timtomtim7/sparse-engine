package blue.sparse.engine.render.scene.component

import blue.sparse.math.vectors.floats.Vector3f
import java.lang.Math.pow

class VelocityComponent(val component: Transformed, val velocity: Vector3f): Component by component
{
	override fun update(delta: Float)
	{
		component.transform.translate(velocity * delta)
		velocity *= pow(0.8, delta.toDouble()).toFloat()
		component.update(delta)
	}
}