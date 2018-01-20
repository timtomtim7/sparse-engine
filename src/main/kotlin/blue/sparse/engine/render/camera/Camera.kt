package blue.sparse.engine.render.camera

import blue.sparse.math.FloatTransform
import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.floats.*

class Camera(
		var projection: Matrix4f,
		val transform: FloatTransform = FloatTransform(),
		var controller: CameraController? = null
)
{
	val viewMatrix get() = transform.inverseMatrix
	val viewProjectionMatrix get() = projection * viewMatrix

	fun update(delta: Float)
	{
		controller?.update(delta)
	}

	fun lookAt(target: Vector3f, up: Vector3f = Axis.Y.vector3)
	{
		transform.lookAway(-target, up)
	}

	fun moveTo(destination: Vector3f)
	{
		transform.setTranslation(destination)
	}

	fun move(movement: Vector3f)
	{
		transform.translate(-movement)
	}

	fun move(direction: Vector3f, amount: Float)
	{
		move(normalize(direction) * amount)
	}

	companion object
	{
		fun orthographic(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float, controller: CameraController? = null): Camera
		{
			return Camera(Matrix4f.orthographic(left, right, bottom, top, near, far), controller = controller)
		}

		fun perspective(fov: Float, aspectRatio: Float, near: Float, far: Float, controller: CameraController? = null): Camera
		{
			return Camera(Matrix4f.perspective(fov, aspectRatio, near, far), controller = controller)
		}
	}
}