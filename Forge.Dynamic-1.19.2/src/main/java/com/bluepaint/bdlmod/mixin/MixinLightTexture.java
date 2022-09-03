package com.bluepaint.bdlmod.mixin;

import com.bluepaint.bdlmod.BlueDynamicLightsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class MixinLightTexture {

    @Inject(method = "<init>", at = @At("TAIL"))
    public void LightTexture(GameRenderer gameRenderer, Minecraft minecraft, CallbackInfo ci) {
        BlueDynamicLightsMod.init(minecraft);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        BlueDynamicLightsMod.tick();
    }

    @Inject(method = "updateLightTexture", at = @At("TAIL"))
    public void updateLightTexture(float f, CallbackInfo ci) {
        BlueDynamicLightsMod.render();
    }

    @Inject(method = "close", at = @At("TAIL"))
    public void close(CallbackInfo ci) {
        BlueDynamicLightsMod.close();
    }

    @Inject(method = "turnOnLightLayer", at = @At("TAIL"))
    public void turnOnLightLayer(CallbackInfo ci) {
        BlueDynamicLightsMod.activateLights();
    }

    @Inject(method = "turnOffLightLayer", at = @At("TAIL"))
    public void turnOffLightLayer(CallbackInfo ci) {
        BlueDynamicLightsMod.deactivateLights();
    }
}
