#version 150

#moj_import <fog.glsl>
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
uniform int FogShape;

uniform int NumLights;
uniform vec3 CamPos;

out float vertexDistance;
out vec4 vertexColor;
out vec2 texCoord0;
out vec4 normal;

void main() {
    vec3 pos = Position + ChunkOffset;
    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(ModelViewMat, pos, FogShape);
    vec4 lightAccum = bdlmod_mix_light(Sampler3, NumLights, pos, CamPos);
    vertexColor = Color * (minecraft_sample_lightmap(Sampler2, UV2) + lightAccum);
    texCoord0 = UV0;
    normal = ProjMat * ModelViewMat * vec4(Normal, 0.0);
}
