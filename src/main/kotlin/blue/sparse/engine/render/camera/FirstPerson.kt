package blue.sparse.engine.render.camera

import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window
import blue.sparse.engine.window.input.*
import blue.sparse.math.vectors.floats.*

class FirstPerson(camera: Camera, val mouseSensitivity: Float = 0.17f, val movementSpeed: Float = 6f): CameraController(camera)
{
	private var lastMousePos = Vector2f(0f)

	override fun update(delta: Float)
	{
		val window = SparseEngine.window
		val input = window.input

//		println(window.cursorMode)
//		println(input[Key.ESCAPE].pressed)
//		println(input[MouseButton.LEFT].pressed)

		if (window.cursorMode == Window.CursorMode.NORMAL && (input[Key.ESCAPE].pressed || input[MouseButton.LEFT].pressed))
		{
			println("FirstPerson focused")
			window.cursorMode = Window.CursorMode.DISABLED
			lastMousePos = input.mousePosition
		}

		if (window.cursorMode == Window.CursorMode.DISABLED && (input[Key.ESCAPE].pressed))
		{
			println("FirstPerson unfocused")
			window.cursorMode = Window.CursorMode.NORMAL
		}

		if (window.cursorMode != Window.CursorMode.DISABLED) return

		freeLook(input)
		freeMove(delta, input)
	}

	private fun freeMove(delta: Float, input: Input)
	{
		val movement = Vector3f(0f)

		if(input[Key.W].held) 			movement += Vector3f( 0f,  0f,  1f)
		if(input[Key.S].held) 			movement += Vector3f( 0f,  0f, -1f)
		if(input[Key.D].held) 			movement += Vector3f( 1f,  0f,  0f)
		if(input[Key.A].held) 			movement += Vector3f(-1f,  0f,  0f)
		if(input[Key.SPACE].held) 		movement += Vector3f( 0f,  1f,  0f)
		if(input[Key.LEFT_SHIFT].held) 	movement += Vector3f( 0f, -1f,  0f)

		if(movement.all { it == 0f })
			return

		val rotated = normalize(movement) * camera.transform.rotation
		val speed = movementSpeed * delta

		camera.transform.translate(rotated * speed)
	}

	private fun freeLook(input: Input)
	{
//		if(!input.mouseMoved) return

		val mousePos = input.mousePosition
		val mouseDiff = mousePos - lastMousePos
		lastMousePos = mousePos

		val diff = (mouseDiff * mouseSensitivity) * 0.015f
//		println(diff)

		if(diff.x != 0f) camera.transform.rotateRad(Axis.Y.vector3, diff.x)
		if(diff.y != 0f) camera.transform.rotateRad(camera.transform.rotation.left, diff.y)
	}
}