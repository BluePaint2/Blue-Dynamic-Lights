package com.bluepaint.bdlmod.mixin;

import com.bluepaint.bdlmod.BlueDynamicLightsMod;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ShaderInstance.class)
public abstract class MixinShaderInstance {

    @Shadow
    @Nullable
    public abstract Uniform getUniform(String p_173349_);

    @Nullable
    public Uniform NUM_LIGHTS;
    @Nullable
    public Uniform CAM_POS;
    @Nullable
    public Uniform CAM_ROT;

    @Inject(method = "<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V",at = @At("TAIL"))
    public void ShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_, CallbackInfo ci) {
        this.NUM_LIGHTS = this.getUniform("NumLights");
        this.CAM_POS = this.getUniform("CamPos");
        this.CAM_ROT = this.getUniform("CamRot");
    }

    @Inject(method = "apply", at = @At("HEAD"))
    public void apply(CallbackInfo ci) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThread);
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (NUM_LIGHTS != null) {
            NUM_LIGHTS.set(BlueDynamicLightsMod.getDynamicLightAmount());
        }
        if (CAM_POS != null) {
            CAM_POS.set(new Vector3f(camera.getPosition()));
        }
        if (CAM_ROT != null) {
            Vector3f r = camera.getLeftVector();
            Vector3f u = camera.getUpVector();
            Vector3f f = camera.getLookVector();
            CAM_ROT.setMat4x4(-r.x(), -r.y(), -r.z(), 0, u.x(), u.y(), u.z(), 0, -f.x(), -f.y(), -f.z(), 0, 0.0f, 0.0f, 0.0f, 1.0f);
        }
    }
}
