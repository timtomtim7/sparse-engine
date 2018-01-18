#version 330 core

in vec3 position;
out vec4 color;

void main()
{
	color = vec4((position + 1) / 2, 1);
}