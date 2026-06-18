package dev.obscuria.obscure_tooltips.client.tooltip.label;

import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.Locale;

public final class RarityLabelProvider implements LabelProvider {

    public static final RarityLabelProvider INSTANCE = new RarityLabelProvider();

    @Override
    public TooltipComponent create(ItemStack stack) {
        return TooltipComponent.create(TextFormatting.GRAY + I18n.format(makeRarityKey(stack.getRarity())));
    }

    private static String makeRarityKey(EnumRarity rarity) {
        final String name = rarity.toString().toLowerCase(Locale.US).replace(':', '.');
        if (I18n.hasKey(name)) {
            return name;
        }
        final String namedKey = "rarity." + name + ".name";
        return I18n.hasKey(namedKey) ? namedKey : "rarity." + name;
    }
}
