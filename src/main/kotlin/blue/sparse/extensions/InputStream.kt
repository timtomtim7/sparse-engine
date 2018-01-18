package blue.sparse.extensions

import java.io.InputStream

fun InputStream.readText(): String
{
	return bufferedReader().use { it.readText() }
}