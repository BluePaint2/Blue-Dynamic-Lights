package com.bluepaint.bdlmod.client;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

public class Light {
    private final ItemLike item;
    private Color color;
    private float intensity;
    private float attenuation;

    public Light(ItemLike item, Color color, float intensity, float attenuation) {
        this.item = item;
        this.color = color;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    public Light(Color color, float intensity, float attenuation) {
        this.item = null;
        this.color = color;
        this.intensity = intensity;
        this.attenuation = attenuation;
    }

    @Nullable
    public ItemLike getItemLike() {
        return item;
    }

    public Color getColor() {
        return this.color;
    }

    public float getIntensity() {
        return this.intensity;
    }

    public float getIntensitySq() {
        return getIntensity()*getIntensity();
    }

    public float getAttenuation() {
        return this.attenuation;
    }

    public void setColorR(int r) {
        this.color = new Color(r,this.color.getGreen(),this.color.getBlue());
    }

    public void setColorG(int g) {
        this.color = new Color(this.color.getRed(),g,this.color.getBlue());
    }

    public void setColorB(int b) {
        this.color = new Color(this.color.getRed(),this.color.getGreen(),b);
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public void setAttenuation(float attenuation) {
        this.attenuation = attenuation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return Float.compare(light.intensity, intensity) == 0 && Float.compare(light.attenuation, attenuation) == 0 && Objects.equals(color, light.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, intensity, attenuation);
    }

    public String getTag() {
        CompoundTag compoundTag = new CompoundTag();
        if (this.attenuation > 0f) {
            compoundTag.putFloat("attenuation", this.attenuation);
        }
        if (this.intensity > 0f) {
            compoundTag.putFloat("intensity", this.intensity);
        }
        compoundTag.putInt("color_B",this.color.getBlue());
        compoundTag.putInt("color_G",this.color.getGreen());
        compoundTag.putInt("color_R",this.color.getRed());
        if (this.item != null) {
            compoundTag.putString("item", ForgeRegistries.ITEMS.getKey(this.item.asItem()).toString());
        }
        return compoundTag.toString();
    }
}
