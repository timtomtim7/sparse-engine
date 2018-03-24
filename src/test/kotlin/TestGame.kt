import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.WavefrontModelLoader
import blue.sparse.engine.render.camera.PanOrbit
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.render.scene.component.ModelComponent
import blue.sparse.engine.render.scene.component.VelocityComponent
import blue.sparse.engine.window.input.Key
import blue.sparse.extensions.nextQuaternion4f
import blue.sparse.extensions.nextVector3f
import blue.sparse.math.vectors.floats.Vector3f
import blue.sparse.math.vectors.floats.normalize
import java.util.concurrent.ThreadLocalRandom

class TestGame : SparseGame() {
	private val shader: ShaderProgram = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
	private val texture: Texture = Texture(Asset["textures/developer/diffuse.png"])

	//	private val models = listOf("cube", "cylinder", "ico_sphere", "torus").map { WavefrontModelLoader.load(Asset["models/$it.obj"]) }
	private val models = listOf("curved_edge_cube").map { WavefrontModelLoader.load(Asset["models/$it.obj"]) }
	private val random get() = ThreadLocalRandom.current()

	init {
//		val asset = Asset["sparse_icon_64.png"]
//		texture.subImage(asset, Vector2i(128, 128))

		camera.apply {
			moveTo(normalize(Vector3f(1f, 1f, 1f)) * 10f)
			lookAt(Vector3f(0f))
			controller = PanOrbit(this)
		}
	}

	private fun addElement() {
		val model = models[random.nextInt(models.size)]
		val component = ModelComponent(model, arrayOf(texture))

//		val translation = random.nextVector3f(-16f, 16f)
		val rotation = random.nextQuaternion4f()
//		val scale = random.nextVector3f(1f, 2f)

//			component.transform.setTranslation(translation)
		component.transform.setRotation(rotation)
//		component.transform.setScale(scale)

		scene.add(VelocityComponent(component, random.nextVector3f(-20f, 20f)))
	}

	override fun update(delta: Float) {
		if (input[Key.E].held)
			addElement()

		camera.update(delta)
		scene.update(delta)
	}

	override fun render(delta: Float) {
		engine.clear()
		scene.render(delta, camera, shader)
	}
}