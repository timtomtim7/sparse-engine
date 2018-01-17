package blue.sparse.engine

class WindowBuilder(var width: Int = 1280, var height: Int = 720)
{
	var resizable: Boolean = false
	var maximized: Boolean = false
	var preserveAspectRatio: Boolean = false
	var mode: Window.Mode = Window.Mode.NORMAL
	var title: String = "Untitled"
	var visible: Boolean = true
	var parent: Window? = null
	var vSync: Boolean = false
	var cursorMode: Window.CursorMode = Window.CursorMode.NORMAL

	fun width(width: Int): WindowBuilder
	{
		this.width = width
		return this
	}

	fun height(height: Int): WindowBuilder
	{
		this.height = height
		return this
	}

	fun resizable(resizable: Boolean): WindowBuilder
	{
		this.resizable = resizable
		return this
	}

	fun resizable() = resizable(true)

	fun maximized(maximized: Boolean): WindowBuilder
	{
		this.maximized = maximized
		return this
	}

	fun maximized() = maximized(true)

	fun preserveAspectRatio(preserveAspectRatio: Boolean): WindowBuilder
	{
		this.preserveAspectRatio = preserveAspectRatio
		return this
	}

	fun preserveAspectRatio() = preserveAspectRatio(true)

	fun mode(mode: Window.Mode): WindowBuilder
	{
		this.mode = mode
		return this
	}

	fun borderless() = mode(Window.Mode.BORDERLESS)

	fun fullscreen() = mode(Window.Mode.FULLSCREEN)

	fun borderlessFullscreen() = mode(Window.Mode.BORDERLESS_FULLSCREEN)

	fun title(title: String): WindowBuilder
	{
		this.title = title
		return this
	}

	fun visible(visible: Boolean): WindowBuilder
	{
		this.visible = visible
		return this
	}

	fun parent(parent: Window?): WindowBuilder
	{
		this.parent = parent
		return this
	}

	fun vSync(vSync: Boolean): WindowBuilder
	{
		this.vSync = vSync
		return this
	}

	fun cursorMode(cursorMode: Window.CursorMode): WindowBuilder
	{
		this.cursorMode = cursorMode
		return this
	}

	fun build(): Window
	{
		return Window(Window.Initial(title, width, height, resizable, maximized, preserveAspectRatio, mode, visible, parent, vSync, cursorMode))
	}
}
