package blue.sparse.engine.render.scene.component

import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.model.Model
import blue.sparse.engine.render.resource.shader.ShaderProgram

class ModelComponent(val model: Model, val textures: Array<Texture>) : Transformed()
{
	override fun render(delta: Float, camera: Camera, shader: ShaderProgram)
	{
		shader.uniforms["uModel"] = modelMatrix

		for ((index, texture) in textures.withIndex())
			texture.bind(index)
		model.render()
	}
}