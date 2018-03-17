#version 330 core

in vec3 aPosition;
out vec3 vPosition;

uniform mat4 uProjection;
uniform mat4 uRotation;

void main()
{
	vPosition = aPosition;

	gl_Position = vec4(aPosition, 1) * (uRotation * uProjection);
}