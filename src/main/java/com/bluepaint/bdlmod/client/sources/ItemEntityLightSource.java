package com.bluepaint.bdlmod.client.sources;

import com.bluepaint.bdlmod.BlueDynamicLightsMod;
import com.bluepaint.bdlmod.client.IDynamicLightSource;
import com.bluepaint.bdlmod.client.LightUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.awt.*;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = BlueDynamicLightsMod.MODID,value = Dist.CLIENT,bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemEntityLightSource implements IDynamicLightSource {

    private final ItemEntity itemEntity;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (event.side != LogicalSide.CLIENT || clientLevel == null) return;
        for (Entity entity : clientLevel.entitiesForRendering()) {
            if (entity instanceof ItemEntity itemEntity) {
                ItemEntityLightSource lightSource = new ItemEntityLightSource(itemEntity);
                if (lightSource.active()) {
                    BlueDynamicLightsMod.addLightSource(lightSource); //add if light is active
                }else {
                    BlueDynamicLightsMod.removeLightSource(lightSource); //remove if light is inactive
                }
            }
        }
    }

    public ItemEntityLightSource(ItemEntity itemEntity) {
        this.itemEntity = itemEntity;
    }


    @Override
    public boolean active() {
        return LightUtil.contains(this.itemEntity.getItem().getItem()) && itemEntity.isAddedToWorld() && this.isInCamRange();
    }

    public boolean isInCamRange() {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        return this.itemEntity.shouldRender(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z); //when far away stops rendering permanently, this should fix that
    }

    @Override
    public float intensity() {
        return LightUtil.getLight(this.itemEntity.getItem().getItem()).getIntensitySq();
    }

    @Override
    public Color color() {
        return LightUtil.getLight(this.itemEntity.getItem().getItem()).getColor();
    }

    @Override
    public Vec3 position() {
        return this.itemEntity.position();
    }

    @Override
    public float attenuation() {
        return LightUtil.getLight(this.itemEntity.getItem().getItem()).getAttenuation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; //use so isDuplicate will work properly
        ItemEntityLightSource that = (ItemEntityLightSource) o;
        return Objects.equals(this.itemEntity, that.itemEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.itemEntity);
    }
}
