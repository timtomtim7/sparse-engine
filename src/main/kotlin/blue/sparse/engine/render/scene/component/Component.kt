package blue.sparse.engine.render.scene.component

import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.shader.ShaderProgram

interface Component
{
	val overridesShader: Boolean get() = false

	fun update(delta: Float) {}

	fun render(delta: Float, camera: Camera, shader: ShaderProgram) {}
}