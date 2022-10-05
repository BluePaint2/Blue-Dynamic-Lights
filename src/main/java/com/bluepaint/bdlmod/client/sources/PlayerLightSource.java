package com.bluepaint.bdlmod.client.sources;

import com.bluepaint.bdlmod.BlueDynamicLightsMod;
import com.bluepaint.bdlmod.client.IDynamicLightSource;
import com.bluepaint.bdlmod.client.LightUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;

import java.awt.*;
import java.util.Objects;

public class PlayerLightSource implements IDynamicLightSource {

    private final Player player;

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.side != LogicalSide.CLIENT) return;
        Player player = event.player;
        PlayerLightSource lightSource = new PlayerLightSource(player);
        if (lightSource.active()) {
            BlueDynamicLightsMod.addLightSource(lightSource); //add if light is active
        }else {
            BlueDynamicLightsMod.removeLightSource(lightSource); //remove if light is inactive
        }
    }

    public PlayerLightSource(Player player) {
        this.player = player;
    }

    private Item getItem(Player player) {
        float intensity = this.getItemLight(player.getMainHandItem().getItem());
        float intensity2 = this.getItemLight(player.getOffhandItem().getItem());
        return intensity > intensity2 ? player.getMainHandItem().getItem() : player.getOffhandItem().getItem();
    }

    private float getItemLight(Item item) {
        return LightUtil.contains(item) ? LightUtil.getLight(item).getIntensity() : 0f;
    }

    @Override
    public boolean active() {
        return LightUtil.contains(getItem(player)) && player.isAlive() && !player.isSpectator() && player.isAddedToWorld() && !player.isRemoved() && this.isInCamRange();
    }

    public boolean isInCamRange() {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        return this.player.shouldRender(camera.getPosition().x,camera.getPosition().y,camera.getPosition().z); //when far away stops rendering permanently, this should fix that
    }

    @Override
    public float intensity() {
        return LightUtil.getLight(getItem(this.player)).getIntensitySq();
    }

    @Override
    public Color color() {
        return LightUtil.getLight(getItem(this.player)).getColor();
    }

    @Override
    public Vec3 position() {
        return this.player.getEyePosition();
    }

    @Override
    public float attenuation() {
        return LightUtil.getLight(getItem(this.player)).getAttenuation();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false; //use so isDuplicate will work properly
        PlayerLightSource that = (PlayerLightSource) o;
        return Objects.equals(this.player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.player);
    }
}
