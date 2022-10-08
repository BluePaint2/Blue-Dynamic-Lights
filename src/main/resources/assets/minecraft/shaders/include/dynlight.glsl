#version 150

vec4 bdlmod_mix_block_light(sampler2D s2d, int nls, vec3 p, vec3 cp, vec3 n) {
    vec3 lC = vec3(0.0);
    for(int i=0; i < nls; i++) {
        vec4 lightLocation = texelFetch(s2d,ivec2(2*i,0),0);
        vec4 color = texelFetch(s2d,ivec2(2*i+1,0),0);
        float intensity = color.a;
        vec3 realPos = ((lightLocation.xyz - cp) - p);
        float dist = length(realPos);
        float attenuation = lightLocation.a * dist * dist;
        lC = lC + clamp(intensity / attenuation,0,1) * color.rgb;
    }
    lC = clamp(lC,0,1);
    return vec4(lC.r,lC.g,lC.b, 0.0);
}

vec4 bdlmod_mix_entity_light(sampler2D s2d, int nls, vec3 p, vec3 cp, mat4 cr) {
    vec3 lC = vec3(0.0);
    for(int i=0; i < nls; i++) {
        vec4 lightLocation = texelFetch(s2d,ivec2(2*i,0),0);
        vec4 color = texelFetch(s2d,ivec2(2*i+1,0),0);
        float intensity = color.a;
        vec3 realPos = (((lightLocation - vec4(cp,1)) * cr).xyz - p);
        float dist = length(realPos);
        float attenuation = lightLocation.a * dist * dist;
        lC = lC + clamp(intensity / attenuation,0,1) * color.rgb;
    }
    lC = clamp(lC,0,1);
    return vec4(lC.r,lC.g,lC.b, 0.0);
}