package com.bluepaint.bdlmod;

import com.bluepaint.bdlmod.client.DynamicLightTexture;
import com.bluepaint.bdlmod.client.IDynamicLightSource;
import com.bluepaint.bdlmod.client.LightUtil;
import com.bluepaint.bdlmod.client.sources.ItemEntityLightSource;
import com.bluepaint.bdlmod.client.sources.PlayerLightSource;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.awt.*;

@Mod(BlueDynamicLightsMod.MODID)
public class BlueDynamicLightsMod {
    public static final String MODID = "bdlmod";
    private static DynamicLightTexture texture;

    public BlueDynamicLightsMod() {
        if (FMLEnvironment.dist.isClient()) {
            MinecraftForge.EVENT_BUS.addListener(PlayerLightSource::playerTick);
            MinecraftForge.EVENT_BUS.addListener(ItemEntityLightSource::clientTick);
            this.registerLights();
        }
    }

    private void registerLights() {
        //Item Registry
        LightUtil.light(Items.TORCH,new Color(255, 210, 155),14f,15f); //can be overridden by new calls
        LightUtil.light(Items.LANTERN,new Color(249, 175, 115),15f,15f);
        LightUtil.light(Items.CAMPFIRE,new Color(239, 155, 112),15f,15f);
        LightUtil.light(Items.REDSTONE_TORCH,new Color(255, 116, 112),7f,15f);
        LightUtil.light(Items.SOUL_TORCH,new Color(123, 250, 254),10f,15f);
        LightUtil.light(Items.SOUL_LANTERN,new Color(109, 237, 241),10f,15f);
        LightUtil.light(Items.SOUL_CAMPFIRE,new Color(124, 242, 245),10f,15f);
        LightUtil.light(Items.SEA_LANTERN,new Color(226, 235, 228),15f,15f);
        LightUtil.light(Items.SEA_PICKLE,new Color(216, 255, 214),6f,15f);
        LightUtil.light(Items.GLOWSTONE,new Color(249, 231, 174),15f,15f);
        LightUtil.light(Items.JACK_O_LANTERN,new Color(255, 194, 100),15f,15f);
        LightUtil.light(Items.BEACON,new Color(143, 211, 207),15f,15f);
        LightUtil.light(Items.SHROOMLIGHT,new Color(254, 172, 109),15f,15f);
        LightUtil.light(Items.AMETHYST_CLUSTER,new Color(207, 160, 243),5f,15f);
        LightUtil.light(Items.SMALL_AMETHYST_BUD,new Color(207, 160, 243),1f,15f);
        LightUtil.light(Items.MEDIUM_AMETHYST_BUD,new Color(207, 160, 243),2f,15f);
        LightUtil.light(Items.LARGE_AMETHYST_BUD,new Color(207, 160, 243),4f,15f);
    }

    public static int getDynamicLightAmount() {
        return texture.getDynamicLightAmount();
    }

    public static boolean hasDuplicate(IDynamicLightSource lightSource) {
        return texture.getLightSources().contains(lightSource);
    }

    public static void addLightSource(IDynamicLightSource lightSource) {
        if (texture != null && !hasDuplicate(lightSource)) {
            texture.addLight(lightSource);
        }
    }

    public static void removeLightSource(IDynamicLightSource lightSource) {
        if (texture != null) {
            texture.removeLight(lightSource);
        }
    }

    public static void init(Minecraft minecraft) {
        texture = new DynamicLightTexture(minecraft);
    }

    public static void tick() {
        texture.tick();
    }

    public static void render() {
        texture.activateLights();
        texture.updateTexture();
        texture.deactivateLights();
    }

    public static void close() {
        texture.close();
    }

    public static void activateLights() {
        texture.activateLights();
    }

    public static void deactivateLights() {
        texture.deactivateLights();
    }
}
