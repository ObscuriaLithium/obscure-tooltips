package dev.obscuria.obscure_tooltips.client.tooltip.label;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.TextFormatting;

@Desugar
public record TranslatableLabelProvider(String key) implements LabelProvider {

    public static TranslatableLabelProvider fromJson(JsonObject json) {
        return new TranslatableLabelProvider(JsonUtils.getString(json, "key"));
    }

    @Override
    public TooltipComponent create(ItemStack stack) {
        return TooltipComponent.create(TextFormatting.GRAY + I18n.format(key));
    }
}
