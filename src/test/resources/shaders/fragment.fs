#version 330 core

uniform sampler2D texture;

in vec2 vTexCoord;
out vec4 fColor;

void main()
{
	fColor = texture2D(texture, vTexCoord);
}