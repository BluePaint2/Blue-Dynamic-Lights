package com.bluepaint.bdlmod.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.level.ItemLike;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class LightUtil {
    private static final Map<ItemLike,Light> lightMap = new HashMap<>();

    public static Light light(ItemLike item, Color color, float intensity, float attenuation) {
        Light light = new Light(item,color,intensity,attenuation);
        lightMap.put(item,light);
        return light;
    }

    public static boolean contains(ItemLike item) {
        if (item == null) return false;
        return  lightMap.containsKey(item);
    }

    public static Light getLight(ItemLike item) {
        return lightMap.get(item);
    }

    public static void prepareImage(int p_85299_, int p_85301_, int p_85302_) {
        RenderSystem.assertOnRenderThreadOrInit();
        bind(p_85299_);
        GlStateManager._texParameter(3553, 33085, 0);
        GlStateManager._texParameter(3553, 33082, 0);
        GlStateManager._texParameter(3553, 33083, 0);
        GlStateManager._texParameter(3553, 34049, 0.0F);
        GlStateManager._texImage2D(3553, 0, GL30.GL_RGBA32F, p_85301_, p_85302_, 0, GL11.GL_RGBA, GL11.GL_FLOAT, (IntBuffer)null);
    }

    private static void bind(int p_85310_) {
        RenderSystem.assertOnRenderThreadOrInit();
        GlStateManager._bindTexture(p_85310_);
    }
}
