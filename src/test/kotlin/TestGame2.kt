import blue.sparse.engine.SparseGame
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.asset.model.WavefrontModelLoader
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.camera.FirstPerson
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.render.scene.component.ModelComponent
import blue.sparse.engine.render.scene.component.Skybox
import blue.sparse.engine.window.input.Key
import blue.sparse.math.vectors.floats.Vector3f
import blue.sparse.math.vectors.floats.normalize
import org.lwjgl.opengl.GL11.*

class TestGame2 : SparseGame()
{
	val shader = ShaderProgram(Asset["shaders/fragment.fs"], Asset["shaders/vertex.vs"])
	val texture = Texture(Asset["textures/developer/diffuse.png"])

	init
	{
		scene.add(Skybox(Asset["textures/skybox.png"]))

		camera.apply {
			moveTo(normalize(Vector3f(1f, 1f, 1f)) * 10f)
			lookAt(Vector3f(0f))
			controller = FirstPerson(this)
		}
		scene.add(ModelComponent(WavefrontModelLoader.load(Asset["models/noclue_ship.obj"]), arrayOf(texture)))
	}

	override fun update(delta: Float)
	{
		super.update(delta)

		val wireframeButton = input[Key.F]
		if(wireframeButton.pressed)
		{
			glCall { glPolygonMode(GL_FRONT_AND_BACK, GL_LINE) }
			glCall { glDisable(GL_CULL_FACE) }
		}else if(wireframeButton.released)
		{
			glCall { glPolygonMode(GL_FRONT_AND_BACK, GL_FILL) }
			glCall { glEnable(GL_CULL_FACE) }
		}
	}

	override fun render(delta: Float)
	{
		scene.render(delta, camera, shader)
	}
}