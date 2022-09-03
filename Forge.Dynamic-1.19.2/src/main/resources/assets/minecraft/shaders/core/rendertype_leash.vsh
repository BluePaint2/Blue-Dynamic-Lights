#version 150

#moj_import <dynlight.glsl>

in vec3 Position;
in vec4 Color;
in ivec2 UV2;

uniform sampler2D Sampler2;
uniform sampler2D Sampler3;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform vec4 ColorModulator;

uniform int NumLights;
uniform vec3 CamPos;
uniform mat4 CamRot;

out float vertexDistance;
flat out vec4 vertexColor;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);

    vertexDistance = length((ModelViewMat * vec4(Position, 1.0)).xyz);
    vec4 lightAccum = bdlmod_mix_entity_light(Sampler3, NumLights, Position, CamPos, CamRot);
    vertexColor = Color * ColorModulator * (texelFetch(Sampler2, UV2 / 16, 0) + lightAccum);
}
