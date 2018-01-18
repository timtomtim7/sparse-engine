#version 330 core

layout(location = 0) in vec2 vPosition;

out vec3 position;

void main()
{
	position = vec3(vPosition, 0);
	gl_Position = vec4(vPosition, 0, 1);
}