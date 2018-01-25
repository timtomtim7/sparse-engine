import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window

fun main(args: Array<String>)
{
//	PolygonModelLoader.load(Asset["models/uv_sphere.ply"])

	val window = Window(1280, 720) {
		resizable()
		icon("sparse_icon_64.png")
		vSync(false)
	}

	SparseEngine.start(window, TestGame2::class, 100.0)
}