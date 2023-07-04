package com.obscuria.tooltips.registry;

import com.google.gson.JsonObject;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
@SuppressWarnings("all")
public interface TooltipElement<T> {
    T build(JsonObject element);
    default T get() {
        return build(null);
    }
}
