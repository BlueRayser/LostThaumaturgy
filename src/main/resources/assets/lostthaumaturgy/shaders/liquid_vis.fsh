/**
* This shader was created by APengu.
* Don't copy/steal it without any permission!
*/
#version 120

uniform vec2 resolution;
uniform float time;
uniform float alpha;
uniform vec3 color;

uniform sampler2D texture;

vec2 hash(in vec2 p)
{
    return cos(time + sin(mat2(17., 5., 3., 257.) * p - p) * 1234.5678);
}

float noise( in vec2 p )
{
	const float K1	= (sqrt(3.)-1.)/2.;
	const float K2	= (3.-sqrt(3.))/6.;
	vec2 i 		= floor(p + (p.x + p.y) * K1);
	vec2 a 		= p - i + (i.x + i.y)*K2;
    	vec2 o 		= (a.x > a.y) ? vec2(1., 0.) : vec2(0., 1.);
    	vec2 b 		= a - o + K2;
    	vec2 c 		= a - 1.0 + 2.0 * K2;
    	vec3 h 		= (.5 - vec3(dot(a, a), dot(b, b), dot(c, c))) * 3.;
    	vec3 n 		= vec3(dot(a, hash(i)), dot(b, hash(i + o)), dot(c, hash(i+1.0)));
    	return dot(n, h*h*h*h*h) * 0.5 + 0.5;
}

void main()
{
    float colorMod = 1.05;
	vec2 p 	= gl_FragCoord.xy / resolution;
	p 	= 2.0 * p - 1.0;
	p.x 	*= resolution.x / resolution.y;


	p += 10.;
	p *= .5;
	//p += mouse * 100.;

	float f = 2.;
	float a = .5;
	float n = 0.2;
	for(int i = 0; i < 8; i++)
	{
		n += noise(p * f + noise(p * f ) * 0.5) * a;
		p = p.yx;
		f *= 2.;
		a *= .5;

	}

	vec3 ccol = color;
	vec3 color = color * colorMod * n;
	gl_FragColor = vec4(color, alpha);
}
