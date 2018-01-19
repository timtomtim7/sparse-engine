#version 330 core

attribute vec2 aPosition;
attribute vec3 aColor;

out vec3 vColor;

void main()
{
	vColor = aColor;

	gl_Position = vec4(aPosition, 0, 1);
}