#version 150

#moj_import <dynlight.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D Sampler2;
uniform sampler2D Sampler3;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

uniform int NumLights;
uniform vec3 CamPos;

out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vec4 lightAccum = bdlmod_mix_light(Sampler3, NumLights, Position, CamPos);
    vertexColor = Color * (texelFetch(Sampler2, UV2 / 16, 0) + lightAccum);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
