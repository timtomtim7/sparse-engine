package blue.sparse.engine

import blue.sparse.engine.errors.printOpenGLErrors
import blue.sparse.engine.window.Window
import blue.sparse.math.util.DeltaTimer
import blue.sparse.math.util.FrequencyTimer
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11

class SparseEngine(val window: Window, val game: SparseGame, val targetFrameRate: Double = 0.0)
{
	var frameRate: Double = 0.0
		private set

	init
	{
//		if (GL.getCapabilities() == null)
			GL.createCapabilities()
	}

	fun start()
	{
		GL.createCapabilities()
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
				frameRate = frameCounter / secondTimer.count

				frameCounter++
				if (secondTimer.count >= 1)
				{
					val frames = frameCounter / secondTimer.count
					secondTimer.use()
					val ms = 1000.0 / frames

					println(String.format("FPS: %8.3f | MS: %5.2f", frames, ms))
					frameCounter -= frames
					secondTimer.use()
				}
			}
			window.pollEvents()
		}

		window.destroy()
	}
}