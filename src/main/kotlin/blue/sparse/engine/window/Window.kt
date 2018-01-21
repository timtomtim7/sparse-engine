package blue.sparse.engine.window

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.GLFWException
import blue.sparse.engine.util.ColorFormat
import blue.sparse.engine.window.input.Input
import blue.sparse.extensions.toByteBuffer
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.glfw.GLFWImage
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

	internal val id: Long

	val requestingClose: Boolean
		get() = glfwWindowShouldClose(id)

	var width: Int
		get() = intArrayOf(0).apply { glfwGetWindowSize(id, this, null as IntArray?) }[0]
		set(value) = glfwSetWindowSize(id, value, height)

	var height: Int
		get() = intArrayOf(0).apply { glfwGetWindowSize(id, null as IntArray?, this) }[0]
		set(value) = glfwSetWindowSize(id, width, value)

	val aspectRatio: Float
		get() = width.toFloat() / height.toFloat()

	var vSync: Boolean = true
		set(value)
		{
			field = value
			glfwSwapInterval(if(value) 1 else 0)
		}

	var visible: Boolean
		get() = glfwGetWindowAttrib(id, GLFW_VISIBLE) == GLFW_TRUE
		set(value) = when(value)
		{
			true -> glfwShowWindow(id)
			false -> glfwHideWindow(id)
		}

	val resizable: Boolean get() = glfwGetWindowAttrib(id, GLFW_RESIZABLE) == GLFW_TRUE
	val focused: Boolean get() = glfwGetWindowAttrib(id, GLFW_FOCUSED) == GLFW_TRUE
	val iconified: Boolean get() = glfwGetWindowAttrib(id, GLFW_ICONIFIED) == GLFW_TRUE
	val maximized: Boolean get() = glfwGetWindowAttrib(id, GLFW_MAXIMIZED) == GLFW_TRUE

	val mode: Mode

	var cursorMode: CursorMode
		get() = CursorMode[glfwGetInputMode(id, GLFW_CURSOR)]
		set(value) = glfwSetInputMode(id, GLFW_CURSOR, value.id)

	val input: Input

	var icon: Asset? = null
		set(value)
		{
			field = value
			updateIcon(value)
		}

	init
	{
		GLFWErrorCallback.createPrint(System.err).set()

		if (!glfwInit()) throw GLFWException("Unable to initialize GLFW.")

		glfwDefaultWindowHints()
//		glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
		glfwWindowHint(GLFW_DECORATED, if (initial.mode.decorated) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_RESIZABLE, if (initial.resizable) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_MAXIMIZED, if (initial.maximized) GLFW_TRUE else GLFW_FALSE)
		glfwWindowHint(GLFW_SAMPLES, 4)

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

		id = glfwCreateWindow(
				width, height,
				initial.title,
				if (initial.mode.fullscreen) primaryMonitor else 0,
				initial.parent?.id ?: 0
		)

		if (id == 0L) throw GLFWException("Window creation failed.")
		glfwMakeContextCurrent(id)

		if(!initial.mode.fullscreen && !initial.maximized)
			glfwSetWindowPos(id, (vidMode.width() - initial.width) / 2, (vidMode.height() - initial.height) / 2)

		if (initial.preserveAspectRatio)
			glfwSetWindowAspectRatio(id, width, height)

		glfwSetWindowRefreshCallback(id) { glViewport(0, 0, this.width, this.height) }

		glfwShowWindow(id)
		vSync = initial.vSync
		mode = initial.mode
		cursorMode = initial.cursorMode
		icon = initial.icon

		input = Input(this)
	}

	fun iconify() = glfwIconifyWindow(id)
	fun maximize() = glfwMaximizeWindow(id)
	fun restore() = glfwRestoreWindow(id)
	fun focus() = glfwFocusWindow(id)

	fun requestAttention() = glfwRequestWindowAttention(id)

	private fun updateIcon(asset: Asset?)
	{
		if(asset == null)
		{
			val empty = GLFWImage.malloc(0)
			glfwSetWindowIcon(id, empty)
			empty.free()

			return
		}

		val image = asset.readImage()
		val glfwImage = GLFWImage.malloc()
		glfwImage.set(image.width, image.height, image.toByteBuffer(ColorFormat.RGBA))

		val glfwBuffer = GLFWImage.malloc(1)
		glfwBuffer.put(0, glfwImage)

		glfwSetWindowIcon(id, glfwBuffer)

		glfwBuffer.free()
		glfwImage.free()
	}

	fun pollEvents()
	{
		glfwPollEvents()
	}

	fun swapBuffers()
	{
		glfwSwapBuffers(id)
	}

	fun destroy(terminate: Boolean = true)
	{
		glfwDestroyWindow(id)
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
			val cursorMode: CursorMode = CursorMode.NORMAL,
			val icon: Asset? = null
	)

	companion object
	{
		inline operator fun invoke(width: Int = 1280, height: Int = 720, body: WindowBuilder.() -> Unit): Window
		{
			return WindowBuilder(width, height).apply(body).build()
		}
	}
}
