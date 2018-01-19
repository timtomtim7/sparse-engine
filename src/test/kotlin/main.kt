import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window

fun main(args: Array<String>)
{
	val window = Window(1280, 720) {
		resizable()
		preserveAspectRatio()
	}

	val engine = SparseEngine(window, TestGame::class)

	engine.start()
}