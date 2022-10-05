package com.bluepaint.bdlmod.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;

public class DynamicLightMap extends AbstractTexture {
    private static final Logger LOGGER = LogManager.getLogger();
    @Nullable
    private DynamicLightNativeImage pixels;

    public DynamicLightMap(int p_117980_, int p_117981_) {
        RenderSystem.assertThread(RenderSystem::isOnGameThreadOrInit);
        this.pixels = new DynamicLightNativeImage(p_117980_, p_117981_);
        LightUtil.prepareImage(this.getId(), this.pixels.getWidth(), this.pixels.getHeight());
    }

    public void load(ResourceManager p_117987_) {
    }

    public void upload() {
        if (this.pixels != null) {
            this.bind();
            this.pixels.upload(0);
        } else {
            LOGGER.warn("Trying to upload disposed texture {}", (int)this.getId());
        }

    }

    @Nullable
    public DynamicLightNativeImage getPixels() {
        return this.pixels;
    }

    public void close() {
        if (this.pixels != null) {
            this.pixels.close();
            this.releaseId();
            this.pixels = null;
        }

    }
}
