package blue.sparse.extensions

import blue.sparse.math.PI
import blue.sparse.math.vectors.floats.*
import java.util.Random

fun Random.nextQuaternion4f(): Quaternion4f
{
	return Quaternion4f(nextDirection(), nextFloat() * PI.toFloat())
}

fun Random.nextVector3f(min: Float, max: Float): Vector3f
{
	val range = max - min
	if(range < 0f) throw IllegalArgumentException("Range must be positive")

	return Vector3f(nextFloat() * range + min, nextFloat() * range + min, nextFloat() * range + min)
}

fun Random.nextDirection(): Vector3f
{
	return normalize(nextVector3f(-1f, 1f))
}