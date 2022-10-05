package com.bluepaint.bdlmod.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class DynamicLightNativeImage implements AutoCloseable {
    private final int width;
    private final int height;
    private final FloatBuffer pixels;

    public DynamicLightNativeImage(int p_84973_, int p_84974_) { //ALWAYS RGBA
        if (p_84973_ > 0 && p_84974_ > 0) {
            this.width = p_84973_;
            this.height = p_84974_;
            int size = p_84973_ * p_84974_ * 4;
            this.pixels = MemoryUtil.memAllocFloat(size);
        } else {
            throw new IllegalArgumentException("Invalid texture size: " + p_84973_ + "x" + p_84974_);
        }
    }

    @Override
    public void close() {
        if (this.pixels != null) {
            MemoryUtil.memFree(this.pixels);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    private void checkAllocated() {
        if (this.pixels == null) {
            throw new IllegalStateException("Image is not allocated.");
        }
    }

    private boolean isOutsideBounds(int p_166423_, int p_166424_) {
        return p_166423_ < 0 || p_166423_ >= this.width || p_166424_ < 0 || p_166424_ >= this.height;
    }

    private static void setFilter(boolean p_85082_, boolean p_85083_) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        if (p_85082_) {
            GlStateManager._texParameter(3553, 10241, p_85083_ ? 9987 : 9729);
            GlStateManager._texParameter(3553, 10240, 9729);
        } else {
            GlStateManager._texParameter(3553, 10241, p_85083_ ? 9986 : 9728);
            GlStateManager._texParameter(3553, 10240, 9728);
        }

    }

    public void setPixelRGBA(int x, int y, float f) {
        if (!this.isOutsideBounds(x, y)) {
            this.checkAllocated();
            for (var i=0; i<4; i++) {
                int shift = ((x + y * this.width) * 4)+i;
                this.pixels.put(shift,f);
            }
        }
    }

    public void setPixelRGBA(int x, int y, float r, float g, float b, float a) {
        if (!this.isOutsideBounds(x, y)) {
            this.checkAllocated();
            for (var i=0; i<4; i++) {
                int shift = ((x + y * this.width) * 4)+i;
                this.pixels.put(shift,i == 0 ? r : i == 1 ? g : i == 2 ? b : a);
            }
        }
    }

    public void upload(int level) {
        this.upload(level, this.width, this.height, false);
    }

    public void upload(int level, int width, int height, boolean p_85011_) {
        this.upload(level, width, height, false, p_85011_);
    }

    public void upload(int level, int width, int height, boolean p_85021_, boolean p_85023_) {
        if (!RenderSystem.isOnRenderThreadOrInit()) {
            RenderSystem.recordRenderCall(() -> {
                this._upload(level, width, height, p_85021_, p_85023_);
            });
        } else {
            this._upload(level, width, height, p_85021_, p_85023_);
        }

    }

    private void _upload(int level, int width, int height, boolean p_85098_, boolean p_85100_) {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        this.checkAllocated();
        setFilter(p_85098_, p_85100_);
        RenderSystem.disableBlend();
        GL11.glPixelStorei(3314, 0);
        GL11.glPixelStorei(3317, 4);
        GL11.glPixelStorei(3316, 0);
        GL11.glPixelStorei(3315, 0);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexImage2D(3553, level, GL30.GL_RGBA32F, width, height, 0, GL11.GL_RGBA, GL11.GL_FLOAT, this.pixels);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
    }
}
