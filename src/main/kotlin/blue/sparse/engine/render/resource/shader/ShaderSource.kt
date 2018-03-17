package blue.sparse.engine.render.resource.shader

import blue.sparse.engine.asset.Asset
import java.util.ArrayList
import java.util.HashMap

class ShaderSource private constructor(val name: String, val lines: List<Line>)
{
//	val uniforms = parseUniforms()

	fun getContent() = lines.joinToString("\n") { it.source }

//	private fun parseUniforms(): List<SourceUniform>
//	{
//		val result = ArrayList<SourceUniform>()
//
//		lines.forEach { line ->
//			val uniformMatcher = uniformRegex.matchEntire(line.source)
//			if(uniformMatcher != null)
//			{
//				val type = uniformMatcher.groupValues[1]
//				val name = uniformMatcher.groupValues[2]
//				result.add(SourceUniform(type, name, line))
//			}
//		}
//
//		return result
//	}

	companion object
	{
		val nameRegex = Regex("\\s*#name \"?([^\"]+)\"?\\s*")
		val localIncludeRegex = Regex("\\s*#include <([^\"<>]+)>\\s*")
		val fileIncludeRegex = Regex("\\s*#include \"([^\"<>]+)\"(:\"([^\"<>]+)\")?\\s*")

		val uniformRegex = Regex("(?:^|\\n|\\r)\\s*uniform\\s+([a-zA-Z0-9_]+)\\s+([a-zA-Z0-9_]+)\\s*;")

		private const val GLOBAL = "__global__"

		fun load(asset: Asset): Collection<ShaderSource>
		{
			val content = asset.readText()

			val lines = content
					.replace("\r", "")
					.split("\n")
					.mapIndexed { index: Int, source: String -> Line(source, index + 1, asset.path) }

			val namespaces = HashMap<String, MutableList<Line>>()
			var currentName = GLOBAL

			lines.forEach {
				val newName = nameRegex.matchEntire(it.source)?.groupValues?.get(1)
				if (newName != null)
					currentName = newName
				else
					namespaces.getOrPut(currentName, { ArrayList() }).add(it)
			}

			for ((name, nLines) in namespaces)
			{
				if(name == GLOBAL) continue

				nLines.addAll(0,namespaces[GLOBAL] ?: emptyList())

				val newLines = ArrayList<Line>()
				for(line in nLines)
				{
					val localIncludeMatcher = localIncludeRegex.matchEntire(line.source)
					if(localIncludeMatcher != null)
					{
						val toIncludeName = localIncludeMatcher.groupValues[1]
						if(toIncludeName == name) throw GLShaderPreprocessorException("Attempted to include self ${line.link}")
						val toInclude = namespaces[toIncludeName] ?: throw GLShaderPreprocessorException("Could not find local namespace \"$toIncludeName\" ${line.link}")
						newLines.addAll(toInclude)
						continue
					}

					val fileIncludeMatcher = fileIncludeRegex.matchEntire(line.source)
					if(fileIncludeMatcher != null)
					{
						val toIncludeFile = fileIncludeMatcher.groupValues[1]
						var toIncludeName = fileIncludeMatcher.groupValues[2]
						if(toIncludeName.isEmpty()) toIncludeName = GLOBAL

						if(toIncludeFile == line.originalFileName)
							throw GLShaderPreprocessorException("Attempted to include self ${line.link}")

						val toInclude = load(Asset[toIncludeFile]).find { it.name == toIncludeName} ?: throw GLShaderPreprocessorException("Could not find namespace \"$toIncludeName\" in file \"$toIncludeFile\" ${line.link}")
						newLines.addAll(toInclude.lines)
						continue
					}

					newLines.add(line)
				}

				nLines.clear()
				nLines.addAll(newLines)
			}

			return namespaces.map { (k, v) -> ShaderSource(k, v) }
		}
	}

	data class Line(val source: String, val lineNumber: Int, val originalFileName: String)
	{
		val link = "at ?.?(${originalFileName.split(Regex("[/\\\\]")).last()}:$lineNumber)"
	}

	class GLShaderPreprocessorException(msg: String = "") : Exception (
			if(msg.isEmpty())
				"An error occurred in the GLSL preprocessor"
			else
				"An error occurred in the GLSL preprocessor: $msg"
	)
}

