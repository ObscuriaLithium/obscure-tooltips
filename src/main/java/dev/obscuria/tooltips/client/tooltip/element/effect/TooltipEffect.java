package dev.obscuria.tooltips.client.tooltip.element.effect;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface TooltipEffect {

    Map<ResourceLocation, Function<JsonObject, TooltipEffect>> REGISTRY = new HashMap<>();

    boolean canApply(List<TooltipEffect> effects);

    default void renderIcon(TooltipState state, GuiGraphics graphics, int x, int y) {}

    default void renderBack(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {}

    default void renderFront(TooltipState state, GuiGraphics graphics, int x, int y, int width, int height) {}

    static void register(String name, Function<JsonObject, TooltipEffect> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipEffect fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipEffect> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown effect type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("rim_light", RimLightEffect::fromJson);
        register("ray_glow", RayGlowEffect::fromJson);
        register("inward_particle", InwardParticleEffect::fromJson);
        register("icon_particle", IconParticleEffect::fromJson);
        register("shimmer", ShimmerEffect::fromJson);
        register("glint", GlintEffect::fromJson);
    }
}
