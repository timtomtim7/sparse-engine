package blue.sparse.engine.render.camera

import blue.sparse.math.FloatTransform
import blue.sparse.math.matrices.Matrix4f

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