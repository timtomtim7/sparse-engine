import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.PolygonModelLoader
import blue.sparse.engine.asset.model.WavefrontModelLoader
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.camera.PanOrbit
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.render.scene.Scene
import blue.sparse.engine.render.scene.component.ModelComponent
import blue.sparse.engine.render.scene.component.VelocityComponent
import blue.sparse.engine.window.input.Key
import blue.sparse.extensions.nextQuaternion4f
import blue.sparse.extensions.nextVector3f
import blue.sparse.math.vectors.floats.Vector3f
import blue.sparse.math.vectors.floats.normalize
import java.util.concurrent.ThreadLocalRandom

class TestGame : SparseGame()
{
	private val shader: ShaderProgram = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
	private val texture: Texture = Texture(Asset["textures/developer/diffuse.png"])

	private val scene = Scene()

	private val camera = Camera.perspective(100f, window.aspectRatio, 0.1f, 1000f).apply {
		moveTo(normalize(Vector3f(1f, 1f, 1f)) * 10f)
		lookAt(Vector3f(0f))
		controller = PanOrbit(this)
	}

	private val models = listOf("cube", "cylinder", "ico_sphere", "torus").map { WavefrontModelLoader.load(Asset["models/$it.obj"]) }
	private val random get() = ThreadLocalRandom.current()

	init
	{
		PolygonModelLoader.load(Asset["models/cube.ply"])

//		for(i in 0..64)
//		{
//			addElement()
//		}
	}

	private fun addElement()
	{
		val model = models[random.nextInt(models.size)]
		val component = ModelComponent(model, arrayOf(texture))

		val translation = random.nextVector3f(-16f, 16f)
		val rotation = random.nextQuaternion4f()
		val scale = random.nextVector3f(1f, 2f)

//			component.transform.setTranslation(translation)
		component.transform.setRotation(rotation)
		component.transform.setScale(scale)

		scene.add(VelocityComponent(component, random.nextVector3f(-20f, 20f)))
	}

	override fun update(delta: Float)
	{
		if(input[Key.E].held)
			addElement()

		camera.update(delta)
		scene.update(delta)
	}

	override fun render(delta: Float)
	{
		scene.render(delta, camera, shader)
	}
}