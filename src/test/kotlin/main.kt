import blue.sparse.engine.SparseEngine
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.window.Window

fun main(args: Array<String>)
{
//	AssetManager.registerProvider(AssetArchive(File("test.zip")))
//	AssetManager.registerProvider(AssetArchive(File("test.jar")))
//	AssetManager.registerProvider(AssetDirectory(File("test")))

//	println(Asset["words.txt"].readText())
//	println(Asset["folder/words2.txt"].readText())

	val window = Window(1280, 720) {
		resizable()
		preserveAspectRatio()
	}

	window.setIcon(Asset["sparse_icon_64.png"])

	val engine = SparseEngine(window, TestGame::class)

	engine.start()
}