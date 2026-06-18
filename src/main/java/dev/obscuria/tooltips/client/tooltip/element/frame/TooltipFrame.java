package dev.obscuria.tooltips.client.tooltip.element.frame;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TooltipFrame {

    Map<ResourceLocation, Function<JsonObject, TooltipFrame>> REGISTRY = new HashMap<>();

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    static void register(String name, Function<JsonObject, TooltipFrame> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipFrame fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipFrame> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown frame type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("blank", json -> BlankFrame.INSTANCE);
        register("nine_sliced", NineSlicedFrame::fromJson);
    }
}
