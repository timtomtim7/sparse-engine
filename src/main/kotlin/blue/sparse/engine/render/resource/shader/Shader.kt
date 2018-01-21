package blue.sparse.engine.render.resource.shader

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.errors.ResourceException
import blue.sparse.engine.errors.glCall
import blue.sparse.engine.render.resource.Resource
import blue.sparse.logger.Logger
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER

class Shader(sourceAsset: Asset, type: Type) : Resource()
{
	internal val id: Int

	init
	{
		val source = sourceAsset.readText()
		id = glCall { glCreateShader(type.id) }
		if (id == 0) throw ResourceException("Unable to create shader.")

		glCall { glShaderSource(id, source) }
		glCall { glCompileShader(id) }
		val log = glGetShaderInfoLog(id)
		if (glGetShaderi(id, GL_COMPILE_STATUS) == 0)
			throw ResourceException("Shader compile error: ($type, ${sourceAsset.path}): \n$log")

		if(log.isNotBlank())
			Logger.debug("Shader info log: $log")
	}

	override fun release()
	{
		glCall { glDeleteShader(id) }
	}

	enum class Type(internal val id: Int)
	{
		VERTEX(GL_VERTEX_SHADER),
		FRAGMENT(GL_FRAGMENT_SHADER),
		GEOMETRY(GL_GEOMETRY_SHADER)
	}
}