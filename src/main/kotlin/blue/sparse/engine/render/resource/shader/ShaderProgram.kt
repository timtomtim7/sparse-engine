package blue.sparse.engine.render.resource.shader

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.ResourceException
import blue.sparse.engine.render.resource.Bindable
import blue.sparse.engine.render.resource.Resource
import blue.sparse.engine.render.resource.shader.uniform.Uniforms
import org.lwjgl.opengl.GL20.*

class ShaderProgram(shaders: Collection<Shader>) : Resource(), Bindable
{
	internal val id: Int
	val uniforms: Uniforms

	constructor(vararg shaders: Shader): this(shaders.toSet())

	constructor(vararg shadersAssets: Pair<Asset, Shader.Type>): this(shadersAssets.map { Shader(it.first, it.second) })

	constructor(fragment: Asset? = null, vertex: Asset? = null, geometry: Asset? = null): this(
			*listOfNotNull(
					fragment?.let { it to Shader.Type.FRAGMENT },
					vertex?.let { it to Shader.Type.VERTEX },
					geometry?.let { it to Shader.Type.GEOMETRY }
			).toTypedArray()
	)

	init
	{
		id = glCreateProgram()
		shaders.forEach {
			glAttachShader(id, it.id)
		}

		glLinkProgram(id)
		if (glGetProgrami(id, GL_LINK_STATUS) == 0)
			throw ResourceException("Shader program linking error: \n${glGetProgramInfoLog(id)}")

		glValidateProgram(id)
		if (glGetProgrami(id, GL_VALIDATE_STATUS) == 0)
			throw ResourceException("Shader program validation error: \n${glGetProgramInfoLog(id)}")

		uniforms = Uniforms(this)
	}

	override fun bind()
	{
		glUseProgram(id)
	}

	override fun unbind()
	{
		glUseProgram(0)
	}

	override fun release()
	{
		glDeleteProgram(id)
	}
}