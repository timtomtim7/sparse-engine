import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window

fun main(args: Array<String>)
{
//	PolygonModelLoader.load(Asset["models/uv_sphere.ply"])

	val window = Window(
			args.getOrNull(0)?.toIntOrNull() ?: 1600,
			args.getOrNull(1)?.toIntOrNull() ?: 900
	) {
		resizable()
		icon("sparse_icon_64.png")
		vSync(true)
	}

	SparseEngine.start(window, TestGame::class, 100.0)
}