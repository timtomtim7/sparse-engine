#version 330 core

in vec2 aPosition;
in vec2 aTexCoord;

out vec2 vTexCoord;

uniform mat4 uViewProj;

void main()
{
	vTexCoord = aTexCoord;
	gl_Position = vec4(aPosition, 0, 1) * uViewProj;
}