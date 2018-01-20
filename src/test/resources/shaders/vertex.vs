#version 330 core

attribute vec3 aPosition;
attribute vec2 aTexCoord;

out vec2 vTexCoord;

uniform mat4 model;
uniform mat4 viewProj;

void main()
{
	vTexCoord = aTexCoord;
	gl_Position = vec4(aPosition, 1) * viewProj;
}