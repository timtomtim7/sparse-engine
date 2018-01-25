package blue.sparse.engine.render

import blue.sparse.engine.SparseEngine
import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.Texture
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.model.*
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.math.matrices.Matrix4f
import blue.sparse.math.vectors.floats.Vector2f

class Logo
{
	private val aspectRatio = SparseEngine.window.aspectRatio
	private val viewProj = Matrix4f.orthographic(-aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)

	private val array = VertexArray()
	private val layout = VertexLayout()
	private val buffer = VertexBuffer()
	private val model = IndexedModel(array, intArrayOf(0, 1, 2, 0, 2, 3))
	private val texture = Texture(Asset["sparse_logo.png"])

	private val shader = ShaderProgram(Asset["shaders/logo.fs"], Asset["shaders/logo.vs"])

	init
	{
		layout.add<Vector2f>()
		layout.add<Vector2f>()

		buffer.add(Vector2f(-1f, -1f) / 1.2f, Vector2f(0f, 1f))
		buffer.add(Vector2f(-1f,  1f) / 1.2f, Vector2f(0f, 0f))
		buffer.add(Vector2f( 1f,  1f) / 1.2f, Vector2f(1f, 0f))
		buffer.add(Vector2f( 1f, -1f) / 1.2f, Vector2f(1f, 1f))

		array.add(buffer, layout)
	}

	fun render()
	{
		bind(shader, texture) {
			shader.uniforms["uViewProj"] = viewProj
			model.render()
		}
	}

	fun delete()
	{
		shader.delete()
		texture.delete()
		model.delete()
		array.delete()
	}
}