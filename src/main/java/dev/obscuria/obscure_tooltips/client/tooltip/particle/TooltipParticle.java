package dev.obscuria.obscure_tooltips.client.tooltip.particle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.ParticleData;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TooltipParticle {
    Map<ResourceLocation, Function<JsonObject, TooltipParticle>> REGISTRY = new HashMap<>();

    void render(GuiGraphics graphics, TooltipState state, ParticleData data);

    static void register(String name, Function<JsonObject, TooltipParticle> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipParticle fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipParticle> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown particle type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("texture", TextureParticle::fromJson);
        register("line", LineParticle::fromJson);
    }
}
