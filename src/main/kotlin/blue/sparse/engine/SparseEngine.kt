package blue.sparse.engine

import org.lwjgl.opengl.GL

class SparseEngine(val window: Window)
{
	init
	{
		if (GL.getCapabilities() == null)
			GL.createCapabilities()
	}

	fun start()
	{

	}
}