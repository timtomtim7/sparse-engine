import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.WavefrontModelLoader
import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.camera.FirstPerson
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.render.scene.Scene
import blue.sparse.engine.render.scene.component.ModelComponent
import blue.sparse.math.PI
import blue.sparse.math.vectors.floats.*
import java.util.concurrent.ThreadLocalRandom

class TestGame : SparseGame()
{
	private val shader: ShaderProgram = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
	private val texture: Texture = Texture(Asset["textures/developer/diffuse.png"])

	private val scene = Scene()

	private val camera = Camera.perspective(100f, window.aspectRatio, 0.1f, 1000f).apply {
		moveTo(normalize(Vector3f(1f, 1f, 1f)) * 10f)
		lookAt(Vector3f(0f))
		controller = FirstPerson(this)
	}

	init
	{
		val pRange = 32f
		val pRange2 = pRange / 2f

		val sRange = 2f
		val sRangeMin = 1f
//		val sRange2 = sRange / 2f

		val models = listOf("cube", "cylinder", "ico_sphere", "torus").map { WavefrontModelLoader.load(Asset["models/$it.obj"]) }
		val random = ThreadLocalRandom.current()
		for(i in 0..128)
		{
			val model = models[random.nextInt(models.size)]
			val component = ModelComponent(model, arrayOf(texture))

			val translation = Vector3f(random.nextFloat() * pRange - pRange2, random.nextFloat() * pRange - pRange2, random.nextFloat() * pRange - pRange2)
			val rotationAxis = normalize(Vector3f(random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1, random.nextFloat() * 2 - 1))
			val rotationAngle = random.nextFloat() * PI.toFloat() * 2
			val scale = Vector3f(random.nextFloat() * sRange + sRangeMin, random.nextFloat() * sRange + sRangeMin, random.nextFloat() * sRange + sRangeMin)

			component.transform.setTranslation(translation)
			component.transform.setRotation(Quaternion4f(rotationAxis, rotationAngle))
			component.transform.setScale(scale)

			scene.add(component)
		}
	}

	override fun update(delta: Float)
	{
		camera.update(delta)
		scene.update(delta)
	}

	override fun render(delta: Float)
	{
		scene.render(delta, camera, shader)
	}
}