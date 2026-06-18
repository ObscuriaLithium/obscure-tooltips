package dev.obscuria.obscure_tooltips.client.tooltip.element.slot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TooltipSlot {

    Map<ResourceLocation, Function<JsonObject, TooltipSlot>> REGISTRY = new HashMap<>();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void register(String name, Function<JsonObject, TooltipSlot> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipSlot fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipSlot> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown slot type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("blank", json -> BlankSlot.INSTANCE);
        register("color_rect", ColorRectSlot::fromJson);
    }
}
