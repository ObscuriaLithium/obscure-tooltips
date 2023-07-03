package com.obscuria.obscuretooltips.client;

import com.google.common.collect.ImmutableList;
import com.obscuria.obscuretooltips.client.style.TooltipStylePreset;
import com.obscuria.obscuretooltips.client.style.TooltipStyle;
import com.obscuria.obscuretooltips.client.style.effect.TooltipEffect;
import com.obscuria.obscuretooltips.client.style.frame.TooltipFrame;
import com.obscuria.obscuretooltips.client.style.icon.TooltipIcon;
import com.obscuria.obscuretooltips.client.style.panel.TooltipPanel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TooltipsAssociations {
    private static final HashMap<Item, TooltipStylePreset> ASSOCIATIONS = new HashMap<>();

    public static void associate(Item item, TooltipStylePreset preset) {
        ASSOCIATIONS.put(item, preset);
    }

    public static Optional<TooltipStyle> getStyleFor(ItemStack stack) {
        final TooltipStylePreset preset = ResourceLoader.getStyleFor(stack.getItem())
                .orElse(ASSOCIATIONS.get(stack.getItem()));
        if (preset == null) return defaultStyle(stack);
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(preset.getPanel().orElse(defaultPanel(stack)))
                .withFrame(preset.getFrame().orElse(defaultFrame(stack)))
                .withIcon(preset.getIcon().orElse(defaultIcon(stack)))
                .withEffects(collectEffects(stack, preset.getEffects()))
                .build());
    }

    @Contract("_ -> new")
    public static Optional<TooltipStyle> defaultStyle(ItemStack stack) {
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(defaultPanel(stack))
                .withFrame(defaultFrame(stack))
                .withIcon(defaultIcon(stack))
                .withEffects(collectEffects(stack, ImmutableList.of()))
                .build());
    }

    public static TooltipPanel defaultPanel(ItemStack stack) {
        if (stack.getRarity() == Rarity.EPIC) return TooltipsRegistry.PANEL_GOLDEN;
        if (stack.getRarity() == Rarity.RARE) return TooltipsRegistry.PANEL_SILVER;
        return TooltipsRegistry.PANEL_DEFAULT;
    }

    public static TooltipFrame defaultFrame(ItemStack stack) {
        if (stack.getRarity() == Rarity.EPIC) return TooltipsRegistry.FRAME_GOLDEN;
        if (stack.getRarity() == Rarity.RARE) return TooltipsRegistry.FRAME_SILVER;
        return TooltipsRegistry.FRAME_DEFAULT;
    }

    public static TooltipIcon defaultIcon(ItemStack stack) {
        if (stack.getRarity() == Rarity.EPIC) return TooltipsRegistry.ICON_ANIMATED_EPIC;
        if (stack.getRarity() == Rarity.RARE) return TooltipsRegistry.ICON_ANIMATED;
        if (stack.getRarity() == Rarity.UNCOMMON) return TooltipsRegistry.ICON_ANIMATED;
        return TooltipsRegistry.ICON_DEFAULT;
    }

    public static List<TooltipEffect> collectEffects(ItemStack stack, ImmutableList<TooltipEffect> associated) {
        final List<TooltipEffect> effects = new ArrayList<>(associated);
        if ((stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) && !effects.contains(TooltipsRegistry.ENCHANTMENT_EFFECT))
            effects.add(TooltipsRegistry.ENCHANTMENT_EFFECT);
        if (stack.getItem() == Items.DRAGON_EGG) effects.add(TooltipsRegistry.TAILS_EFFECT);
        return effects;
    }
}
