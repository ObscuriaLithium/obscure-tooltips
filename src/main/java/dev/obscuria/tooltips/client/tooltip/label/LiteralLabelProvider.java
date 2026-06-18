package dev.obscuria.tooltips.client.tooltip.label;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.text.TextFormatting;

@Desugar
public record LiteralLabelProvider(String text) implements LabelProvider {

    public static LiteralLabelProvider fromJson(JsonObject json) {
        return new LiteralLabelProvider(JsonUtils.getString(json, "text"));
    }

    @Override
    public TooltipComponent create(ItemStack stack) {
        return TooltipComponent.create(TextFormatting.GRAY + text);
    }
}
