import blue.sparse.engine.SparseEngine
import blue.sparse.engine.window.Window
import blue.sparse.engine.errors.printOpenGLErrors
import blue.sparse.math.util.FrequencyTimer
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

fun main(args: Array<String>)
{
	val window = Window(1280, 720) {
		resizable()
		preserveAspectRatio()
	}

	val engine = SparseEngine(window, TestGame())

	engine.start()
}