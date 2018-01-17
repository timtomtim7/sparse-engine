package blue.sparse.engine.window

import blue.sparse.engine.errors.GLFWException
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL11.glViewport

class Window(initial: Initial)
{
	constructor(
			title: String = "Untitled",
			width: Int = 1280,
			height: Int = 720,
			resizable: Boolean = false,
			maximized: Boolean = false,
			preserveAspectRatio: Boolean = false,
			mode: Mode = Mode.NORMAL,
			visible: Boolean = true,
			parent: Window? = null
	) : this(Initial(title, width, height, resizable, maximized, preserveAspectRatio, mode, visible, parent))

	private val pointer: Long

	val requestingClose: Boolean
		get() = glfwWindowShouldClose(pointer)

	var width: Int
		get() = intArrayOf(0).apply { glfwGetWindowSize(pointer, this, null as IntArray?) }[0]
		set(value) = glfwSetWindowSize(pointer, value, height)

	var height: Int
		get() = intArrayOf(0).apply { glfwGetWindowSize(pointer, null as IntArray?, this) }[0]
		set(value) = glfwSetWindowSize(pointer, width, value)

	var vSync: Boolean = true
		set(value)
		{
			field = value
			glfwSwapInterval(if(value) 1 else 0)
		}

	var visible: Boolean
		get() = glfwGetWindowAttrib(pointer, GLFW_VISIBLE) == GLFW_TRUE
		set(value) = when(value)
		{
			true -> glfwShowWindow(pointer)
			false -> glfwHideWindow(pointer)
		}

	val resizable: Boolean get() = glfwGetWindowAttrib(pointer, GLFW_RESIZABLE) == GLFW_TRUE
	val focused: Boolean get() = glfwGetWindowAttrib(pointer, GLFW_FOCUSED) == GLFW_TRUE
	val iconified: Boolean get() = glfwGetWindowAttrib(pointer, GLFW_ICONIFIED) == GLFW_TRUE
	val maximized: Boolean get() = glfwGetWindowAttrib(pointer, GLFW_MAXIMIZED) == GLFW_TRUE

	val mode: Mode

	var cursorMode: CursorMode
		get() = CursorMode[glfwGetInputMode(pointer, GLFW_CURSOR)]
		set(value) = glfwSetInputMode(pointer, GLFW_CURSOR, value.id)

	init
	{
		GLFWErrorCallback.createPrint(System.err).set()

		if (!glfwInit()) throw GLFWException("Unable to initialize GLFW.")

		glfwDefaultWindowHints()
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_DECORATED, if (initial.mode.decorated) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, if (initial.resizable) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_MAXIMIZED, if (initial.maximized) GLFW_TRUE else GLFW_FALSE)

		val primaryMonitor = glfwGetPrimaryMonitor()

		val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())

		val width: Int
		val height: Int

		if (initial.mode == Mode.BORDERLESS_FULLSCREEN)
		{
			glfwWindowHint(GLFW_RED_BITS, vidMode.redBits())
			glfwWindowHint(GLFW_GREEN_BITS, vidMode.greenBits())
			glfwWindowHint(GLFW_BLUE_BITS, vidMode.blueBits())
			glfwWindowHint(GLFW_REFRESH_RATE, vidMode.refreshRate())
			width = vidMode.width()
			height = vidMode.height()
		} else
		{
			width = initial.width
			height = initial.height
		}

		pointer = glfwCreateWindow(
				width, height,
				initial.title,
				if (initial.mode.fullscreen) primaryMonitor else 0,
				initial.parent?.pointer ?: 0
		)

		if (pointer == 0L) throw GLFWException("Window creation failed.")
		glfwMakeContextCurrent(pointer)

		if(!initial.mode.fullscreen && !initial.maximized)
			glfwSetWindowPos(pointer, (vidMode.width() - initial.width) / 2, (vidMode.height() - initial.height) / 2)

		if (initial.preserveAspectRatio)
		{
			println("Preserve aspect ratio")
			glfwSetWindowAspectRatio(pointer, width, height)
		}

		glfwSetWindowRefreshCallback(pointer) { glViewport(0, 0, this.width, this.height) }

		glfwShowWindow(pointer)
		vSync = initial.vSync
		mode = initial.mode
		cursorMode = initial.cursorMode
	}

	fun iconify() = glfwIconifyWindow(pointer)
	fun maximize() = glfwMaximizeWindow(pointer)
	fun restore() = glfwRestoreWindow(pointer)
	fun focus() = glfwFocusWindow(pointer)

	fun requestAttention() = glfwRequestWindowAttention(pointer)

	fun pollEvents()
	{
		glfwPollEvents()
	}

	fun swapBuffers()
	{
		glfwSwapBuffers(pointer)
	}

	fun destroy(terminate: Boolean = true)
	{
		glfwDestroyWindow(pointer)
		if (terminate)
			glfwTerminate()
	}

	enum class CursorMode(internal val id: Int)
	{
		NORMAL(GLFW_CURSOR_NORMAL),
		HIDDEN(GLFW_CURSOR_HIDDEN),
		DISABLED(GLFW_CURSOR_DISABLED);

		companion object
		{
			operator fun get(id: Int): CursorMode
			{
				return values().first { it.id == id }
			}
		}
	}

	enum class Mode(val decorated: Boolean, val fullscreen: Boolean)
	{
		NORMAL(true, false),
		BORDERLESS(false, false),
		FULLSCREEN(false, true),
		BORDERLESS_FULLSCREEN(false, true)
	}

	data class Initial(
			val title: String = "Untitled",
			val width: Int = 1280,
			val height: Int = 720,
			val resizable: Boolean = false,
			val maximized: Boolean = false,
			val preserveAspectRatio: Boolean = false,
			val mode: Mode = Mode.NORMAL,
			val visible: Boolean = true,
			val parent: Window? = null,
			val vSync: Boolean = true,
			val cursorMode: CursorMode = CursorMode.NORMAL
	)

	companion object
	{
		inline operator fun invoke(width: Int = 1280, height: Int = 720, body: WindowBuilder.() -> Unit): Window
		{
			return WindowBuilder(width, height).apply(body).build()
		}
	}
}
