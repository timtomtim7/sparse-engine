package blue.sparse.engine.render.scene

import blue.sparse.engine.render.camera.Camera
import blue.sparse.engine.render.resource.bind
import blue.sparse.engine.render.resource.shader.ShaderProgram
import blue.sparse.engine.render.scene.component.Component
import java.util.LinkedHashSet
import java.util.concurrent.ConcurrentLinkedQueue

class Scene
{
	private val components = LinkedHashSet<Component>()
	private val addQueue = ConcurrentLinkedQueue<Component>()
	private val removeQueue = ConcurrentLinkedQueue<Component>()

	fun add(component: Component)
	{
		addQueue.add(component)
	}

	fun remove(component: Component)
	{
		removeQueue.add(component)
	}

	fun update(delta: Float)
	{
		do
		{
			val element = addQueue.poll()
			element?.let(components::add)
		} while(element != null)

		do
		{
			val element = removeQueue.poll()
			element?.let(components::remove)
		} while(element != null)

		components.forEach { it.update(delta) }
	}

	fun render(delta: Float, camera: Camera, shader: ShaderProgram)
	{
		shader.bind {
			shader.uniforms["viewProj"] = camera.viewProjectionMatrix
			components.forEach { it.render(delta, camera, shader) }
		}
	}

	companion object
	{
		inline operator fun invoke(body: Scene.() -> Unit): Scene
		{
			return Scene().apply(body)
		}
	}
}