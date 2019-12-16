#ifdef GL_ES
precision mediump float;
#endif

varying vec4 vColor;
varying vec2 vTexCoord;

//our texture samplers
uniform sampler2D u_texture; //diffuse map

void main() {
	vec4 DiffuseColor = texture2D(u_texture, vTexCoord);
	gl_FragColor = vColor * DiffuseColor;
}