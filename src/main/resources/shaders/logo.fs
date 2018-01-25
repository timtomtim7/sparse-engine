#version 330 core

uniform sampler2D uTexture;

in vec2 vTexCoord;

out vec4 fColor;

void main()
{
	fColor = texture2D(uTexture, vTexCoord);
}