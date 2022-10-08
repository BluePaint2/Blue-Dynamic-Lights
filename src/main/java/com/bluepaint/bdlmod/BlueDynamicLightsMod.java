package com.bluepaint.bdlmod;

import com.bluepaint.bdlmod.client.DynamicLightTexture;
import com.bluepaint.bdlmod.client.IDynamicLightSource;
import com.bluepaint.bdlmod.client.LightConfig;
import com.bluepaint.bdlmod.client.LightUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

@Mod(BlueDynamicLightsMod.MODID)
public class BlueDynamicLightsMod {
    public static final String MODID = "bdlmod";
    private static DynamicLightTexture texture;
    public static LightConfig lightConfig;

    public BlueDynamicLightsMod() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupClient);
    }

    private void setupClient(FMLClientSetupEvent event) {
        LightConfig defaultConfig = new LightConfig();
        File configFile = new File(Minecraft.getInstance().gameDirectory.getAbsolutePath(), File.separatorChar + "config" + File.separatorChar + "bdl.cfg");
        this.registerDefaultLights(defaultConfig,configFile);
        try {
            lightConfig = GsonConfig.loadConfigWithDefault(LightConfig.class, configFile, defaultConfig);
            LightUtil.addLights(lightConfig.getList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerDefaultLights(LightConfig defaultConfig, File configFile) {
        if (!configFile.isFile()) {
            LightUtil.light(defaultConfig, Items.TORCH, new Color(255, 210, 155), 14f, 15f); //can be overridden by new calls
            LightUtil.light(defaultConfig, Items.LANTERN, new Color(249, 175, 115), 15f, 15f);
            LightUtil.light(defaultConfig, Items.CAMPFIRE, new Color(239, 155, 112), 15f, 15f);
            LightUtil.light(defaultConfig, Items.REDSTONE_TORCH, new Color(255, 116, 112), 7f, 15f);
            LightUtil.light(defaultConfig, Items.SOUL_TORCH, new Color(123, 250, 254), 10f, 15f);
            LightUtil.light(defaultConfig, Items.SOUL_LANTERN, new Color(109, 237, 241), 10f, 15f);
            LightUtil.light(defaultConfig, Items.SOUL_CAMPFIRE, new Color(124, 242, 245), 10f, 15f);
            LightUtil.light(defaultConfig, Items.SEA_LANTERN, new Color(226, 235, 228), 15f, 15f);
            LightUtil.light(defaultConfig, Items.SEA_PICKLE, new Color(216, 255, 214), 6f, 15f);
            LightUtil.light(defaultConfig, Items.GLOWSTONE, new Color(249, 231, 174), 15f, 15f);
            LightUtil.light(defaultConfig, Items.JACK_O_LANTERN, new Color(255, 194, 100), 15f, 15f);
            LightUtil.light(defaultConfig, Items.BEACON, new Color(143, 211, 207), 15f, 15f);
            LightUtil.light(defaultConfig, Items.SHROOMLIGHT, new Color(254, 172, 109), 15f, 15f);
            LightUtil.light(defaultConfig, Items.AMETHYST_CLUSTER, new Color(207, 160, 243), 5f, 15f);
            LightUtil.light(defaultConfig, Items.SMALL_AMETHYST_BUD, new Color(207, 160, 243), 1f, 15f);
            LightUtil.light(defaultConfig, Items.MEDIUM_AMETHYST_BUD, new Color(207, 160, 243), 2f, 15f);
            LightUtil.light(defaultConfig, Items.LARGE_AMETHYST_BUD, new Color(207, 160, 243), 4f, 15f);
            LightUtil.light(defaultConfig, Items.OCHRE_FROGLIGHT, new Color(233, 206, 133), 15f, 15f);
            LightUtil.light(defaultConfig, Items.PEARLESCENT_FROGLIGHT, new Color(213, 191, 201), 15f, 15f);
            LightUtil.light(defaultConfig, Items.VERDANT_FROGLIGHT, new Color(161, 211, 155), 15f, 15f);
        }
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

    public static class GsonConfig {

        private static final JsonParser parser = new JsonParser();
        private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

        public static <T> T loadConfigWithDefault(Class<T> clazz, File file, T defaultInstance) throws IOException {
            if (file.createNewFile()) {
                String json = gson.toJson(parser.parse(gson.toJson(defaultInstance)));
                try (PrintWriter out = new PrintWriter(file)) {
                    out.println(json);
                }
                return defaultInstance;
            } else {
                return gson.fromJson(new String(Files.readAllBytes(file.toPath())), clazz);
            }
        }

        public static void saveConfig(Object config, File file) throws IOException {
            if (file.createNewFile()) {
                String json = gson.toJson(parser.parse(gson.toJson(config)));
                try (PrintWriter out = new PrintWriter(file)) {
                    out.println(json);
                }
            }
        }
    }
}
