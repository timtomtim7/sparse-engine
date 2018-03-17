package blue.sparse.engine

import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.scene.Scene

abstract class SparseGame(var camera: Camera = Camera.perspective(100f, SparseEngine.window.aspectRatio, 0.1f, 1000f))
{
	val engine = SparseEngine
	inline val window get() = engine.window
	inline val input get() = window.input

	var scene = Scene()

	open fun postInit() {}

	open fun update(delta: Float)
	{
		camera.update(delta)
		scene.update(delta)
	}

	open fun render(delta: Float)
	{
//		scene.render(delta, camera, )
	}
}