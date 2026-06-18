package dev.obscuria.tooltips.client.tooltip.element.panel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.util.color.ARGB;
import dev.obscuria.tooltips.util.color.Colors;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TooltipPanel {

    Map<ResourceLocation, Function<JsonObject, TooltipPanel>> REGISTRY = new HashMap<>();
    ARGB DEFAULT_SEPARATOR_COLOR = Colors.argbOf(1627389951);

    void render(GuiGraphics graphics, int x, int y, int width, int height);

    default ARGB separatorColor() {
        return DEFAULT_SEPARATOR_COLOR;
    }

    static void register(String name, Function<JsonObject, TooltipPanel> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipPanel fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipPanel> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown panel type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("blank", json -> BlankPanel.INSTANCE);
        register("color_rect", ColorRectPanel::fromJson);
    }
}
