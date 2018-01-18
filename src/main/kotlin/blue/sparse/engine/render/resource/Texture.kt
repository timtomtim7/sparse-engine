package blue.sparse.engine.render.resource

import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glGenTextures

class Texture : Resource(), Bindable
{
	internal val id: Int

	init
	{
		id = glGenTextures()
	}

	override fun bind()
	{

	}

	override fun unbind()
	{

	}

	override fun release()
	{
		glDeleteTextures(id)
	}
}