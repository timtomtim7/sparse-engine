package blue.sparse.engine

import blue.sparse.engine.errors.glCall
import blue.sparse.engine.errors.printOpenGLErrors
import blue.sparse.engine.render.Logo
import blue.sparse.engine.render.resource.Resource
import blue.sparse.engine.util.MemoryUsage
import blue.sparse.engine.window.Window
import blue.sparse.logger.Logger
import blue.sparse.math.util.DeltaTimer
import blue.sparse.math.util.FrequencyTimer
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_MULTISAMPLE
import org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.system.measureTimeMillis

object SparseEngine
{
	lateinit var window: Window
		private set

	lateinit var game: SparseGame
		private set

	var targetFrameRate: Double = 0.0
		private set

	var frameRate: Double = 0.0
		private set

	var running: Boolean = false
		private set

	private fun initGL()
	{
		GL.createCapabilities()
		glCall { glEnable(GL_MULTISAMPLE) }
		glCall { glEnable(GL_CULL_FACE) }
		glCall { glFrontFace(GL_CW) }
		glCall { glCullFace(GL_BACK) }
		glCall { glEnable(GL_DEPTH_TEST) }
		glCall { glEnable(GL_DEPTH_CLAMP) }

		glCall { glEnable(GL_BLEND) }
		glCall { glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA) }

		glClearColor(1f, 1f, 1f, 1f)
		clear()

		Logo().apply {
			render()
			delete()
		}

		window.swapBuffers()

		glCall { glDisable(GL_BLEND) }

		glClearColor(0f, 0f, 0f, 0f)
	}

	fun clear()
	{
		glCall { glClear(GL11.GL_COLOR_BUFFER_BIT or GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT) }
	}

	fun start(window: Window, gameClass: KClass<out SparseGame>, targetFrameRate: Double = 0.0)
	{
		if(running) throw IllegalStateException("Engine already running")
		running = true

		this.window = window
		this.targetFrameRate = targetFrameRate

		initGL()

		val initTime = measureTimeMillis {
			game = gameClass.primaryConstructor!!.call()
			System.gc()
		}

		Logger.info("Game initialization took ${initTime}ms")

		var frameCounter = 0.0
		val frameTimer = FrequencyTimer(1.0 / targetFrameRate)
		val secondTimer = FrequencyTimer(1.0)

		val deltaTimer = DeltaTimer()

		while (!window.requestingClose)
		{
			if (window.vSync || targetFrameRate == 0.0 || frameTimer.use())
			{
//				clear()
				glCall { glClear(GL11.GL_DEPTH_BUFFER_BIT or GL11.GL_STENCIL_BUFFER_BIT) }

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

					Logger.debug(String.format("FPS: %8.3f | MS: %5.2f | RAM: %s", frameRate, ms, MemoryUsage.getMemoryUsedString()))
					frameCounter -= frameRate
				}

				window.input.update()
			}
			window.pollEvents()
		}

		Resource.deleteAll()
		window.destroy()
		running = false
	}
}