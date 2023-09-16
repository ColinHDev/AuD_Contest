#version 120
#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform vec4 v_skinBounds;
uniform sampler2D u_texture;
uniform sampler2D u_skin;
uniform bool flipped;

void main() {
    vec4 v_skinCoords = texture2D(u_texture, v_texCoords);
    if (flipped) {
    texColor = texture2D(u_skin, vec2(v_skinBounds[0] + (1.0 - v_skinCoords.r) * v_skinBounds[2], v_skinBounds[1] + v_skinCoords.g * v_skinBounds[3]));
    }else{
    texColor = texture2D(u_skin, vec2(v_skinBounds[0] + v_skinCoords.r * v_skinBounds[2], v_skinBounds[1] + v_skinCoords.g * v_skinBounds[3]));
    }
    float light = v_skinCoords[2];
    if(light>0.5){
        float tint = light - 0.5;
        texColor = texColor * (1-tint) + vec(1,1,1,1) * tint;
    }else{
        float shade = 0.5 - light;
        texColor = (1 - shade) * texColor;
    }
    texColor.a = v_skinCoords.a;
    gl_FragColor = v_color * texColor;
}