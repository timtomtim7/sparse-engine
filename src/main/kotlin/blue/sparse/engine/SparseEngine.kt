package blue.sparse.engine

import blue.sparse.engine.errors.printOpenGLErrors
import blue.sparse.engine.window.Window
import blue.sparse.math.util.DeltaTimer
import blue.sparse.math.util.FrequencyTimer
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class SparseEngine<G: SparseGame>(val window: Window, val gameClass: KClass<G>, val targetFrameRate: Double = 0.0)
{
	var frameRate: Double = 0.0
		private set

	var running: Boolean = false
		private set

	lateinit var game: G
		private set

	init
	{
//		if (GL.getCapabilities() == null)
//			GL.createCapabilities()
	}

	private fun initGL()
	{
		GL.createCapabilities()
		glEnable(GL_CULL_FACE)
		glFrontFace(GL_CW)
		glCullFace(GL_BACK)
	}

	fun start()
	{
		if(running) throw IllegalStateException("Engine already running")
		running = true

		initGL()

		game = gameClass.primaryConstructor!!.call()
		game.init(this)

		var frameCounter = 0.0
		val frameTimer = FrequencyTimer(1.0 / targetFrameRate)
		val secondTimer = FrequencyTimer(1.0)

		val deltaTimer = DeltaTimer()

		while (!window.requestingClose)
		{
			if (window.vSync || targetFrameRate == 0.0 || frameTimer.use())
			{
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT)

				val delta = deltaTimer.deltaFloat()
				game.update(delta)
				game.render(delta)

				printOpenGLErrors()
				window.swapBuffers()

				frameCounter++
				frameRate = frameCounter / secondTimer.count

				if (secondTimer.use())
				{
					val ms = 1000.0 / frameRate

					println(String.format("FPS: %8.3f | MS: %5.2f", frameRate, ms))
					frameCounter -= frameRate
				}
			}
			window.pollEvents()
		}

		window.destroy()
		running = false
	}
}