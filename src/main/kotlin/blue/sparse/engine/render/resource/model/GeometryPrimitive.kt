package blue.sparse.engine.render.resource.model

import org.lwjgl.opengl.GL11.*

enum class GeometryPrimitive(internal val id: Int) {
	TRIANGLES(GL_TRIANGLES),
	TRIANGLE_STRIP(GL_TRIANGLE_STRIP),
	TRIANGLE_FAN(GL_TRIANGLE_FAN),
	LINES(GL_LINES),
	LINE_STRIP(GL_LINE_STRIP),
	LINE_LOOP(GL_LINE_LOOP),
	POINTS(GL_POINTS),
}