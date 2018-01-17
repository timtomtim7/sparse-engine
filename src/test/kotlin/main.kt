import blue.sparse.engine.Window
import blue.sparse.engine.printOpenGLErrors
import blue.sparse.math.util.FrequencyTimer
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*

fun main(args: Array<String>)
{
	val window = Window(1280, 720) {
		resizable()
		maximized()
	}

	GL.createCapabilities()

	val targetFrameRate = 60.0

	var frameCounter = 0.0
	val frameTimer = FrequencyTimer(1.0 / targetFrameRate)
	val secondTimer = FrequencyTimer(1.0)

	while (!window.requestingClose)
	{
		if (window.vSync || targetFrameRate == 0.0 || frameTimer.use())
		{
			glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT)

			printOpenGLErrors()
			window.swapBuffers()

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

//	glfwInit()
//	val window = glfwCreateWindow(1280, 720, "Test", 0, 0)
//
//	glfwMakeContextCurrent(window)
//	GL.createCapabilities()
//	println(glGetString(GL_VERSION))
//
//	val vbo = glGenBuffers()
//	glBindBuffer(GL_ARRAY_BUFFER, vbo)
//	glBufferData(GL_ARRAY_BUFFER, floatArrayOf(
//			-1f, -1f,
//			0f, 1f,
//			1f, -1f
//	), GL_STATIC_DRAW)
//
//	glEnableVertexAttribArray(0)
//	glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0)
//
//	while (!glfwWindowShouldClose(window))
//	{
//		glClear(GL_COLOR_BUFFER_BIT)
//		glColor3f(1f, 1f, 1f)
//		glDrawArrays(GL_TRIANGLES, 0, 3)
//
//		glfwSwapBuffers(window)
//		glfwPollEvents()
//	}
//
//	glfwTerminate()
}