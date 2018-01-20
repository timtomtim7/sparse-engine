#version 330 core

attribute vec3 aPosition;
attribute vec2 aTexCoord;
attribute vec3 aNormal;

out vec2 vTexCoord;
out vec3 vNormal;

uniform mat4 model;
uniform mat4 viewProj;

void main()
{
	vTexCoord = aTexCoord;
	vNormal = aNormal;
	gl_Position = vec4(aPosition, 1) * viewProj;
}