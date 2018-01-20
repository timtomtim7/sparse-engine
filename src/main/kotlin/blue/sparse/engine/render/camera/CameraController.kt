package blue.sparse.engine.render.camera

abstract class CameraController(val camera: Camera)
{
	abstract fun update(delta: Float)
}