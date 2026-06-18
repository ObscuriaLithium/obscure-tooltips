package dev.obscuria.obscure_tooltips.client.tooltip.label;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public interface LabelProvider {

    Map<ResourceLocation, Function<JsonObject, LabelProvider>> REGISTRY = new HashMap<>();

    TooltipComponent create(ItemStack stack);

    static void register(String name, Function<JsonObject, LabelProvider> factory) {
        REGISTRY.put(ObscureTooltips.resource(name), factory);
    }

    static LabelProvider fromJson(JsonObject json) {
        if (REGISTRY.isEmpty()) {
            bootstrap();
        }
        final ResourceLocation type = new ResourceLocation(JsonUtils.getString(json, "type"));
        final Function<JsonObject, LabelProvider> factory = REGISTRY.get(type);
        if (factory == null) {
            throw new JsonParseException("Unknown label provider type: " + type);
        }
        return factory.apply(json);
    }

    static void bootstrap() {
        register("blank", json -> BlankLabelProvider.INSTANCE);
        register("literal", LiteralLabelProvider::fromJson);
        register("translatable", TranslatableLabelProvider::fromJson);
        register("rarity", json -> RarityLabelProvider.INSTANCE);
    }
}
