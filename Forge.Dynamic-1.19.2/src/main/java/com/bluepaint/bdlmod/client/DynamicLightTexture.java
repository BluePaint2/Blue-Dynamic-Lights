package com.bluepaint.bdlmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DynamicLightTexture {
    private final List<IDynamicLightSource> lightSources = new ArrayList<>();

    private final DynamicLightMap lightTexture;
    public final DynamicLightNativeImage lightPixels;
    private final ResourceLocation lightTextureLocation;
    private boolean updateTexture = false;
    private final Minecraft minecraft;
    private int dynamicLightAmount = 0;

    public DynamicLightTexture(Minecraft p_109879_) {
        this.minecraft = p_109879_;
        this.lightTexture = new DynamicLightMap(1024, 1); //allows 512 lights, half the texture size
        this.lightTextureLocation = new ResourceLocation(String.format(Locale.ROOT, "dynamic/%s", "dynamic_light_map"));
        this.minecraft.getTextureManager().register(this.lightTextureLocation, this.lightTexture);
        this.lightPixels = this.lightTexture.getPixels();
        this.resetPixels();
        this.lightTexture.upload();
    }

    public List<IDynamicLightSource> getLightSources() {
        return lightSources;
    }

    public void addLight(IDynamicLightSource lightSource) {
        this.lightSources.add(lightSource);
    }

    public void removeLight(IDynamicLightSource lightSource) {
        this.lightSources.remove(lightSource);
    }

    public void resetPixels() {
        for(int i = 0; i < this.lightPixels.getHeight(); ++i) {
            for(int j = 0; j < this.lightPixels.getWidth(); ++j) {
                this.lightPixels.setPixelRGBA(j, i, 0f);
            }
        }
    }

    public void close() {
        this.lightTexture.close();
    }

    public void tick() {
        for (var i=0; i<lightSources.size(); i++) {
            IDynamicLightSource lightSource = lightSources.get(i);
            lightSource.tick(updateTexture);
        }
        updateTexture = true;
    }

    public int getDynamicLightAmount() {
        return dynamicLightAmount;
    }

    public void updateTexture() {
        if (updateTexture) {
            this.minecraft.getProfiler().push("dynamicLightTex");
            dynamicLightAmount = 0;
            for (var i = 0; i < lightSources.size(); i++) {
                IDynamicLightSource lightSource = lightSources.get(i);
                lightSource.beforeRender();
                if (lightSource.active()) {
                    Vec3 vec3 = lightSource.position();
                    Color color = lightSource.color();
                    this.lightPixels.setPixelRGBA(2 * dynamicLightAmount, 0, (float) vec3.x, (float) vec3.y, (float) vec3.z, lightSource.attenuation());
                    this.lightPixels.setPixelRGBA(2 * dynamicLightAmount + 1, 0, color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, lightSource.intensity());
                    dynamicLightAmount++;
                }
                lightSource.afterRender();
            }
            updateTexture = false;
            this.lightTexture.upload();
            this.minecraft.getProfiler().pop();
        }
    }

    public void deactivateLights() {
        RenderSystem.setShaderTexture(3, 0);
    }

    public void activateLights() {
        RenderSystem.setShaderTexture(3, this.lightTextureLocation);
        this.minecraft.getTextureManager().bindForSetup(this.lightTextureLocation);
        RenderSystem.texParameter(3553, 10241, 9729);
        RenderSystem.texParameter(3553, 10240, 9729);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

}
