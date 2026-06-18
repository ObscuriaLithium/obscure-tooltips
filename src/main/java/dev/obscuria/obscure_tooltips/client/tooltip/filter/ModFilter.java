package dev.obscuria.obscure_tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

@Desugar
public record ModFilter(List<String> mods) implements ItemFilter {

    public static ModFilter fromJson(JsonObject json) {
        final List<String> mods = new ArrayList<>();
        for (JsonElement element : JsonUtils.getJsonArray(json, "mods")) {
            mods.add(element.getAsString());
        }
        return new ModFilter(mods);
    }

    @Override
    public boolean test(ItemStack stack) {
        final ResourceLocation id = stack.getItem().getRegistryName();
        return id != null && mods.contains(id.getNamespace());
    }
}
