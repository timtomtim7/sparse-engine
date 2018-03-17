#version 330 core

in vec3 vPosition;

out vec4 fColor;

void main()
{
	vec3 dir = normalize(vPosition);

	fColor = vec4(dir * 0.5 + 0.5, 1);
}