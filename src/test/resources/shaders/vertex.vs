#version 330 core

attribute vec2 aPosition;
attribute vec2 aTexCoord;

out vec2 vTexCoord;

void main()
{
	vTexCoord = aTexCoord;
	gl_Position = vec4(aPosition, 0, 1);
}