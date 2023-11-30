#version 120
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
uniform float line_thickness;
uniform vec2 tex_size;
varying vec2 v_texCoords;
uniform vec4 outline_color;
uniform sampler2D u_texture;

float max_f(float f1, float f2) {
    if (f1 > f2) return f1;
    return f2;
}

void main()
{

    vec2 size = vec2(1.0, 1.0) / tex_size * line_thickness;
    vec4 texColor = texture2D(u_texture, v_texCoords);
    if (texColor.a <= 0.1) {
        float a = texture2D(u_texture, v_texCoords + vec2(size.x, 0)).a;
        a += texture2D(u_texture, v_texCoords + vec2(size.x, size.y)).a;
        a += texture2D(u_texture, v_texCoords + vec2(0, size.y)).a;
        a += texture2D(u_texture, v_texCoords + vec2(-size.x, size.y)).a;
        a += texture2D(u_texture, v_texCoords + vec2(size.x, -size.y)).a;
        a += texture2D(u_texture, v_texCoords + vec2(-size.x, 0)).a;
        a += texture2D(u_texture, v_texCoords + vec2(-size.x, -size.y)).a;
        a += texture2D(u_texture, v_texCoords + vec2(0, -size.y)).a;
        texColor = vec4(outline_color.r, outline_color.g, outline_color.b, min(1.0, a));
    }
    gl_FragColor = v_color * texColor;
}
