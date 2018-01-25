package blue.sparse.engine.render.camera

import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.input.Input
import blue.sparse.engine.window.input.MouseButton
import blue.sparse.math.vectors.floats.*

class PanOrbit(
		camera: Camera,
		val focus: Vector3f = Vector3f(0f),
		var panSensitivity: Float = 0.002f,
		var rotSensitivity: Float = 0.005f
) : CameraController(camera)
{
	private var leftStart: Vector2f = Vector2f(0f)
	private var rightStart: Vector2f = Vector2f(0f)

	private var zoomVelocity: Float = 0f

	override fun update(delta: Float)
	{
		val window = SparseEngine.window
		val input = window.input

		pan(input)
		orbit(input)

		val scroll = input.scrollDelta
		if(scroll != 0f)
			zoomVelocity += scroll

		val movement = -(zoomVelocity * delta * 10)
		zoomVelocity += movement
		camera.move(camera.transform.rotation.forward, movement)
	}

	private fun pan(input: Input)
	{
		val panButton = input[MouseButton.MIDDLE]
		if (panButton.pressed) leftStart = input.mousePosition

		if (panButton.held)
		{
			val mousePosition = input.mousePosition
			val transform = camera.transform

			val diff = (leftStart - mousePosition) * distance(-transform.translation, focus) * panSensitivity

			val up = transform.rotation.up
			val movement = (transform.rotation.right * diff.x) + (up * diff.y)

			focus += movement
			camera.move(movement)

			leftStart = mousePosition
		}
	}

	private fun orbit(input: Input)
	{
		val orbitButton = input[MouseButton.RIGHT]
		if(orbitButton.pressed)
			rightStart = input.mousePosition

		if(orbitButton.held)
		{
			val mousePosition = input.mousePosition

			val diff = (mousePosition - rightStart) * rotSensitivity

			val transform = camera.transform
			val rotation = transform.rotation
			val position = -transform.translation

			val offsetFromFocus = position - focus

			val up = Axis.Y.vector3
			val offsetRotationX = Quaternion4f(up, diff.x)
			val offsetRotationY = Quaternion4f(rotation.left, diff.y)
			val offsetRotation = offsetRotationY * offsetRotationX

			val newOffset = offsetFromFocus * offsetRotation
			val newPosition = newOffset + focus

			transform.setTranslation(-newPosition)
			camera.lookAt(focus, up)

			rightStart = mousePosition
		}
	}
}