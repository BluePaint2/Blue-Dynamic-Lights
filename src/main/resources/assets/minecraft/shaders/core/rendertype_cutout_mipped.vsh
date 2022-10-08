#version 150

#moj_import <light.glsl>
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
uniform vec3 ChunkOffset;

uniform int NumLights;
uniform vec3 CamPos;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position + ChunkOffset, 1.0);

    vertexDistance = length((ModelViewMat * vec4(Position + ChunkOffset, 1.0)).xyz);
    vec4 lightAccum = bdlmod_mix_block_light(Sampler3, NumLights, Position + ChunkOffset, CamPos, Normal);
    vertexColor = Color * (lightAccum + minecraft_sample_lightmap(Sampler2, UV2));
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
