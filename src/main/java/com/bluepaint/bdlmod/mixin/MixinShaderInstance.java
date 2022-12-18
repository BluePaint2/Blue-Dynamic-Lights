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

    @Inject(method = "<init>(Lnet/minecraft/server/packs/resources/ResourceProvider;Lnet/minecraft/resources/ResourceLocation;Lcom/mojang/blaze3d/vertex/VertexFormat;)V",at = @At("TAIL"))
    public void ShaderInstance(ResourceProvider p_173336_, ResourceLocation shaderLocation, VertexFormat p_173338_, CallbackInfo ci) {
        this.NUM_LIGHTS = this.getUniform("NumLights");
        this.CAM_POS = this.getUniform("CamPos");
    }

    @Inject(method = "apply", at = @At("HEAD"))
    public void apply(CallbackInfo ci) {
        RenderSystem.assertOnRenderThread();
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        if (NUM_LIGHTS != null) {
            NUM_LIGHTS.set(BlueDynamicLightsMod.getDynamicLightAmount());
        }
        if (CAM_POS != null) {
            CAM_POS.set(new Vector3f(camera.getPosition()));
        }
    }
}
