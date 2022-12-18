#version 150

#moj_import <fog.glsl>
#moj_import <dynlight.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;
uniform sampler2D Sampler3;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

uniform int NumLights;
uniform vec3 CamPos;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = fog_distance(ModelViewMat, Position, FogShape);
    texCoord0 = UV0;
    vec4 lightAccum = bdlmod_mix_light(Sampler3, NumLights, Position, CamPos);
    vertexColor = Color * (texelFetch(Sampler2, UV2 / 16, 0) + lightAccum);
}
