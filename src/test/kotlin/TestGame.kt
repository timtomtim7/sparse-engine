import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.WavefrontModelLoader
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.camera.FirstPerson
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.Model
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.vectors.floats.Vector3f

class TestGame : SparseGame()
{
	private val shader: ShaderProgram = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
	private val texture: Texture = Texture(Asset["textures/developer/diffuse.png"])

	private val model: Model = WavefrontModelLoader.load(Asset["models/test_scene_bigger.obj"])

	private val camera = Camera.perspective(100f, 16f / 9f, 0.1f, 1000f).apply {
		transform.translate(Vector3f(0f, 0f, -10f))
		controller = FirstPerson(this)
	}

	override fun update(delta: Float)
	{
		camera.update(delta)
	}

	override fun render(delta: Float)
	{
		bind(shader, texture) {
			shader.uniforms["viewProj"] = camera.viewProjectionMatrix
			model.render()
		}
	}
}