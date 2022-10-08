package com.bluepaint.bdlmod.client;

import com.bluepaint.bdlmod.BlueDynamicLightsMod;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightUtil {
    private static final Map<ItemLike,Light> lightMap = new HashMap<>();

    public static Light light(LightConfig lightConfig, ItemLike item, Color color, float intensity, float attenuation) {
        Light light = new Light(item,color,intensity,attenuation);
        lightConfig.addString(light.getTag());
        return light;
    }

    public static void addLights(List<String> lightList) {
        for (String json : lightList) {
            try {
                CompoundTag nbt = TagParser.parseTag(json);
                ResourceLocation resourceLocation = new ResourceLocation(nbt.getString("item"));
                Item item = ForgeRegistries.ITEMS.getValue(resourceLocation);
                if (item != null && item != Items.AIR) {
                    nbt.remove("item");
                    float intensity = 0;
                    if(nbt.contains("intensity")) {
                        intensity = nbt.getFloat("intensity");
                        nbt.remove("intensity");
                    }
                    float attenuation = 0f;
                    if(nbt.contains("attenuation")) {
                        attenuation = nbt.getFloat("attenuation");
                        nbt.remove("attenuation");
                    }
                    int red = 0;
                    if(nbt.contains("color_R")) {
                        red = nbt.getInt("color_R");
                        nbt.remove("color_R");
                    }
                    int green = 0;
                    if(nbt.contains("color_G")) {
                        green = nbt.getInt("color_G");
                        nbt.remove("color_G");
                    }
                    int blue = 0;
                    if(nbt.contains("color_B")) {
                        blue = nbt.getInt("color_B");
                        nbt.remove("color_B");
                    }
                    lightMap.put(item, new Light(item,new Color(red,green,blue),intensity,attenuation));
                }
            } catch (CommandSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean contains(ItemLike item) {
        if (item == null) return false;
        return  lightMap.containsKey(item);
    }

    public static Light getLight(ItemLike item) {
        //BlueDynamicLightsMod.lightConfig.
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
