package blue.sparse.engine.render.resource.shader

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.ResourceException
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.resource.Bindable
import blue.sparse.engine.render.resource.Resource
import blue.sparse.engine.render.resource.shader.uniform.Uniforms
import org.lwjgl.opengl.GL20.*

class ShaderProgram(shaders: Collection<Shader>, deleteShaders: Boolean) : Resource(), Bindable
{
	internal val id: Int
	val uniforms: Uniforms

	constructor(vararg shaders: Shader, deleteShaders: Boolean = false): this(shaders.toSet(), deleteShaders)

	constructor(vararg shadersAssets: Pair<Asset, Shader.Type>): this(shadersAssets.map { Shader(it.first, it.second) }, true)

	constructor(fragment: Asset? = null, vertex: Asset? = null, geometry: Asset? = null): this(
			*listOfNotNull(
					fragment?.let { it to Shader.Type.FRAGMENT },
					vertex?.let { it to Shader.Type.VERTEX },
					geometry?.let { it to Shader.Type.GEOMETRY }
			).toTypedArray()
	)

	init
	{
		id = glCall { glCreateProgram() }
		shaders.forEach {
			glCall { glAttachShader(id, it.id) }
		}

		glCall { glLinkProgram(id) }
		if (glGetProgrami(id, GL_LINK_STATUS) == 0)
			throw ResourceException("Shader program linking error: \n${glGetProgramInfoLog(id)}")

		glCall { glValidateProgram(id) }
		if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0)
			throw ResourceException("Shader program validation error: \n${glGetProgramInfoLog(id)}")

		shaders.forEach {
			glCall { glDetachShader(id, it.id) }
			if(deleteShaders)
				it.delete()
		}

		uniforms = Uniforms(this)
	}

	override fun bind()
	{
		glCall { glUseProgram(id) }
	}

	override fun unbind()
	{
		glCall { glUseProgram(0) }
	}

	override fun release()
	{
		glCall { glDeleteProgram(id) }
	}
}