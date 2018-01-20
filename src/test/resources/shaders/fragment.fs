#version 330 core

uniform sampler2D texture;

in vec2 vTexCoord;
in vec3 vNormal;
out vec4 fColor;

void main()
{
	float brightness = dot(vNormal, normalize(vec3(1,1,1))) * 0.5 + 0.5;

	fColor = texture2D(texture, vTexCoord) * brightness;
}