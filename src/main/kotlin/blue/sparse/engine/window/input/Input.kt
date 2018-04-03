package blue.sparse.engine.window.input

import blue.sparse.engine.window.Window
import blue.sparse.math.vectors.floats.Vector2f
import org.lwjgl.glfw.GLFW.*

class Input internal constructor(val window: Window) {
	private val buttons = HashMap<Button, ButtonState>()

	private val _mousePosition = Vector2f(0f)

	private var _textBuffer = StringBuilder()

	var scrollDelta: Float = 0f
		private set

	var mousePosition: Vector2f
		get() = _mousePosition.clone()
		set(value) {
			_mousePosition.assign(value.x, value.y)
			glfwSetCursorPos(window.id, value.x.toDouble(), value.y.toDouble())
		}

	var mouseMoved: Boolean = false
		private set

	val textBuffer: String
		get() = _textBuffer.toString()

	init {
		glfwSetKeyCallback(window.id) { _, keyId, _, action, _ ->
			if (action != GLFW_PRESS && action != GLFW_RELEASE) return@glfwSetKeyCallback

			val state = this[Key[keyId]]
			when (action) {
				GLFW_PRESS -> state.pressed()
				GLFW_RELEASE -> state.released()
			}
		}

		glfwSetMouseButtonCallback(window.id) { _, mouseId, action, _ ->
			if (action != GLFW_PRESS && action != GLFW_RELEASE) return@glfwSetMouseButtonCallback

			val state = this[MouseButton[mouseId]]
			when (action) {
				GLFW_PRESS -> state.pressed()
				GLFW_RELEASE -> state.released()
			}
		}

		glfwSetCursorPosCallback(window.id) { _, x, y ->
			_mousePosition.assign(x.toFloat(), y.toFloat())
			mouseMoved = true
		}

		glfwSetScrollCallback(window.id) { _, _, y ->
			scrollDelta = y.toFloat()
		}

		glfwSetCharCallback(window.id) { _, codePoint ->
			_textBuffer.appendCodePoint(codePoint)
		}
	}

	operator fun get(button: Button): ButtonState {
		return buttons.getOrPut(button) { ButtonState(button) }
	}

	internal fun update() {
		_textBuffer = StringBuilder()
		buttons.values.removeAll {
			it.update()
			it.inactive
		}

		mouseMoved = false
		scrollDelta = 0f
	}

	inner class ButtonState(val button: Button) {
		var pressed = false
			private set

		var held = false
			private set

		var released = false
			private set

		private var pressedStartTime = 0L

		val heldTime: Float
			get() {
				if (pressedStartTime == 0L) return 0f
				return (System.currentTimeMillis() - pressedStartTime) / 1000.0f
			}

		val inactive: Boolean
			get() = !pressed && !held && !released

		internal fun pressed() {
			pressedStartTime = System.currentTimeMillis()
			held = true
			pressed = true
		}

		internal fun released() {
			held = false
			released = true
			pressedStartTime = 0L
		}

		internal fun update() {
			pressed = false
			released = false
		}

		override fun toString(): String {
			return "ButtonState(button=$button, pressed=$pressed, held=$held, released=$released, inactive=$inactive)"
		}
	}
}