package blue.sparse.engine.render.resource.shader

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.ResourceException
import blue.sparse.engine.render.resource.Resource
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER

class Shader(sourceAsset: Asset, type: Type) : Resource()
{
	internal val id: Int

	init
	{
		val source = sourceAsset.readText()
		id = glCreateShader(type.id)
		if (id == 0) throw ResourceException("Unable to create shader.")

		glShaderSource(id, source)
		glCompileShader(id)
		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0)
			throw ResourceException("Shader compile error: ($type, ${sourceAsset.path}): \n${glGetShaderInfoLog(id)}")
	}

	override fun release()
	{
		glDeleteShader(id)
	}

	enum class Type(internal val id: Int)
	{
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER)
	}
}