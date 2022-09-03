package com.bluepaint.bdlmod.client;

import net.minecraft.world.phys.Vec3;

import java.awt.*;

public interface IDynamicLightSource {

    default void tick(boolean updateTexture) {};

    default void beforeRender() {};

    default void afterRender() {};

    boolean active();

    float intensity();

    Color color();

    Vec3 position();

    float attenuation();
}
