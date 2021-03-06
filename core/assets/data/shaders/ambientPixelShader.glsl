#ifdef GL_ES
precision mediump float;
#endif

varying vec4 vColor;
varying vec2 vTexCoord;

//texture samplers
uniform sampler2D u_texture; //diffuse map

//additional parameters for the shader
uniform vec4 ambientColor;

void main() {
	vec4 diffuseColor = texture2D(u_texture, vTexCoord);
	vec3 ambient = ambientColor.rgb * ambientColor.a;
	
	vec3 final = vColor * diffuseColor.rgb * ambient;
	gl_FragColor = vec4(final, diffuseColor.a);
}