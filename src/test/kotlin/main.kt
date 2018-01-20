import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window

fun main(args: Array<String>)
{
	val window = Window(1280, 720) {
		resizable()
		icon("sparse_icon_64.png")
	}

	SparseEngine(window, TestGame::class).start()
}