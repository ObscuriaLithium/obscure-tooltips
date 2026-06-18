package dev.obscuria.obscure_tooltips.client.tooltip.label;

import dev.obscuria.obscure_tooltips.client.component.BlankComponent;
import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import net.minecraft.item.ItemStack;

public final class BlankLabelProvider implements LabelProvider {

    public static final BlankLabelProvider INSTANCE = new BlankLabelProvider();

    @Override
    public TooltipComponent create(ItemStack stack) {
        return BlankComponent.INSTANCE;
    }
}
