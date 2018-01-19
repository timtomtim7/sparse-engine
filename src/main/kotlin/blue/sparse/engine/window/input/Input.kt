package blue.sparse.engine.window.input

import blue.sparse.engine.window.Window
import blue.sparse.math.vectors.floats.Vector2f
import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.GLFW_PRESS
import org.lwjgl.glfw.GLFW.GLFW_RELEASE

class Input internal constructor(val window: Window)
{
	private val buttons = HashMap<Button, ButtonState>()

	private val backingMousePosition = Vector2f(0f)

	val mousePosition: Vector2f
		get() = backingMousePosition.clone()

	var mouseMoved: Boolean = false
		private set

	init
	{
		GLFW.glfwSetKeyCallback(window.id) { _, keyId, _, action, _ ->
			if(action != GLFW_PRESS && action != GLFW_RELEASE) return@glfwSetKeyCallback

			val state = this[Key[keyId]]
			when(action)
			{
				GLFW_PRESS -> state.pressed()
				GLFW_RELEASE -> state.released()
			}
		}

		GLFW.glfwSetMouseButtonCallback(window.id) { _, mouseId, action, _ ->
			if(action != GLFW_PRESS && action != GLFW_RELEASE) return@glfwSetMouseButtonCallback

			val state = this[MouseButton[mouseId]]
			when(action)
			{
				GLFW_PRESS -> state.pressed()
				GLFW_RELEASE -> state.released()
			}
		}

		GLFW.glfwSetCursorPosCallback(window.id) { _, x, y ->
			backingMousePosition.assign(x.toFloat(), y.toFloat())
			mouseMoved = true
		}
	}

	operator fun get(button: Button): ButtonState
	{
		return buttons.getOrPut(button) { ButtonState(button) }
	}

	internal fun update()
	{
		buttons.values.removeAll {
			it.update()
			it.inactive
		}
		mouseMoved = false
	}

	inner class ButtonState(val button: Button)
	{
		var pressed = false
			private set

		var held = false
			private set

		var released = false
			private set

		private var pressedStartTime = 0L

		val heldTime: Float
			get()
			{
				if(pressedStartTime == 0L) return 0f
				return (System.currentTimeMillis() - pressedStartTime) / 1000.0f
			}

		val inactive: Boolean
			get() = !pressed && !held && !released

		internal fun pressed()
		{
			pressedStartTime = System.currentTimeMillis()
			held = true
			pressed = true
		}

		internal fun released()
		{
			held = false
			released = true
			pressedStartTime = 0L
		}

		internal fun update()
		{
			pressed = false
			released = false
		}
	}
}