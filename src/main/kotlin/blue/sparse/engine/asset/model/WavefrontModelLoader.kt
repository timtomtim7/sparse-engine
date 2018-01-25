package blue.sparse.engine.asset.model

import blue.sparse.engine.asset.Asset
import blue.sparse.engine.render.resource.model.*
import blue.sparse.math.vectors.floats.*

object WavefrontModelLoader: ModelLoader
{
	override fun isExtensionSupported(extension: String): Boolean
	{
		return extension == "obj"
	}

	override fun load(asset: Asset): Model
	{
		val lines = asset.readLines()
				.filter { it.isNotBlank() && !it.startsWith("#") }
				.mapNotNull { it.split(" ").takeIf { it.size > 1 } }

		val positions = ArrayList<Vector3f>()
		val texCoords = ArrayList<Vector2f>()
		val normals = ArrayList<Vector3f>()

		val tripleIndices = ArrayList<Triple<Int, Int?, Int?>>()

		fun List<String>.toOptionalIndex(index: Int) = getOrNull(index)?.toIntOrNull()?.let { it - 1 }

		for (line in lines)
		{
			when(line.first())
			{
				"v" -> positions.add(Vector3f(line[1].toFloat(), line[2].toFloat(), line[3].toFloat()))
				"vt" -> texCoords.add(Vector2f(line[1].toFloat(), line[2].toFloat()))
				"vn" -> normals.add(Vector3f(line[1].toFloat(), line[2].toFloat(), line[3].toFloat()))
				"f" -> {
					val faceIndices = ArrayList<Triple<Int, Int?, Int?>>()

					for(i in 1 until line.size)
					{
						val faceIndex = line[i].split("/")

						faceIndices.add(Triple(
								faceIndex[0].toInt() - 1,
								faceIndex.toOptionalIndex(1),
								faceIndex.toOptionalIndex(2)
						))
					}

					if(faceIndices.size == 4)
					{
						tripleIndices.add(faceIndices[0])
						tripleIndices.add(faceIndices[1])
						tripleIndices.add(faceIndices[2])

						tripleIndices.add(faceIndices[0])
						tripleIndices.add(faceIndices[2])
						tripleIndices.add(faceIndices[3])
					}else{
						tripleIndices.addAll(faceIndices)
					}
				}
			}
		}

		val vertices = ArrayList<Vertex>()
		val indices = ArrayList<Int>()

		for (tripleIndex in tripleIndices)
		{
			val position = positions[tripleIndex.first]
			val texCoord = tripleIndex.second?.let(texCoords::get)
			val normal = tripleIndex.third?.let(normals::get)

			val vertex = Vertex(position, texCoord, normal)
			val index = vertices.indexOf(vertex)

			if(index != -1)
			{
				indices.add(index)
			}else{
				indices.add(vertices.size)
				vertices.add(vertex)
			}
		}

		val defaultNormal = Axis.Z.vector3
		val defaultTexCoord = Axis.Z.vector2

		val buffer = VertexBuffer()
		for (vertex in vertices)
		{
			buffer.add(vertex.position)
			buffer.add(vertex.texCoord ?: defaultTexCoord)
			buffer.add(vertex.normal ?: defaultNormal)
		}

		val layout = VertexLayout()
		layout.add<Vector3f>()
		layout.add<Vector2f>()
		layout.add<Vector3f>()

		val array = VertexArray()
		array.add(buffer, layout)

		return IndexedModel(array, indices.toIntArray())
	}

	private data class Vertex(val position: Vector3f, val texCoord: Vector2f?, val normal: Vector3f?)
}