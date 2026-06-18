package dev.obscuria.obscure_tooltips.client.tooltip.element.icon;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.element.Transform;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface TooltipIcon {

    Map<ResourceLocation, Function<JsonObject, TooltipIcon>> REGISTRY = new HashMap<>();

    void render(TooltipState state, GuiGraphics graphics, int x, int y);

    default void pushTransform(TooltipState state, Transform transform, GuiGraphics graphics, int x, int y) {
        graphics.pose().pushMatrix();
        graphics.pose().translate((float)x, (float)y, 150.0f);
        graphics.pose().translate((float) transform.offset().x, (float) transform.offset().y, (float) transform.offset().z);
        graphics.pose().scale(transform.scale(), transform.scale(), transform.scale());
        graphics.pose().rotateZ(transform.rotation());
        applyScale(state, graphics, x, y);
        applyRotation(state, graphics, x, y);
        graphics.pose().pushMatrix();
        graphics.pose().translate(-8.0f, -8.0f, -150.0f);
    }

    default void popTransform(GuiGraphics graphics) {
        graphics.pose().popMatrix();
        graphics.pose().popMatrix();
    }

    default void applyScale(TooltipState state, GuiGraphics graphics, int x, int y) {
    }

    default void applyRotation(TooltipState state, GuiGraphics graphics, int x, int y) {
    }

    static void register(String name, Function<JsonObject, TooltipIcon> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static TooltipIcon fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, TooltipIcon> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown icon type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("static", StaticIcon::fromJson);
        register("accent", AccentIcon::fromJson);
        register("accent_spin", AccentSpinIcon::fromJson);
        register("accent_burst", AccentBurstIcon::fromJson);
    }
}
