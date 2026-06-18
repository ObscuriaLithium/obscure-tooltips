package dev.obscuria.tooltips.client.tooltip.filter;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import dev.obscuria.tooltips.config.BooleanDelegate;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;

@Desugar
public record ConfigFilter(BooleanDelegate configValue) implements ItemFilter {

    public static ConfigFilter fromJson(JsonObject json) {
        final String name = JsonUtils.getString(json, "config_value");
        final BooleanDelegate delegate = BooleanDelegate.byName(name);
        if (delegate == null) {
            throw new JsonParseException("Unknown config value: " + name);
        }
        return new ConfigFilter(delegate);
    }

    @Override
    public boolean test(ItemStack stack) {
        return configValue.value();
    }
}
