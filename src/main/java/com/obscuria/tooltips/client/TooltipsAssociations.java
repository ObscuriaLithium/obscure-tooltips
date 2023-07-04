package com.obscuria.tooltips.client;

import com.google.common.collect.ImmutableList;
import com.obscuria.tooltips.client.style.TooltipStyle;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.TooltipIcon;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
import com.obscuria.tooltips.registry.TooltipsRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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
        return switch (rarityName(stack)) {
            case "EPIC", "LEGENDARY", "MYTHIC" -> TooltipsRegistry.BUILTIN_PANEL_GOLDEN.get();
            case "RARE" -> TooltipsRegistry.BUILTIN_PANEL_SILVER.get();
            default -> TooltipsRegistry.BUILTIN_PANEL_DEFAULT.get();
        };
    }

    public static TooltipFrame defaultFrame(ItemStack stack) {
        return switch (rarityName(stack)) {
            case "EPIC", "LEGENDARY", "MYTHIC" -> TooltipsRegistry.BUILTIN_FRAME_GOLDEN.get();
            case "RARE" -> TooltipsRegistry.BUILTIN_FRAME_SILVER.get();
            default -> TooltipsRegistry.BUILTIN_FRAME_BLANK.get();
        };
    }

    public static TooltipIcon defaultIcon(ItemStack stack) {
        return switch (rarityName(stack)) {
            case "EPIC", "LEGENDARY", "MYTHIC" -> TooltipsRegistry.BUILTIN_ICON_EPIC.get();
            case "UNCOMMON", "RARE" -> TooltipsRegistry.BUILTIN_ICON_RARE.get();
            default -> TooltipsRegistry.BUILTIN_ICON_COMMON.get();
        };
    }

    public static List<TooltipEffect> collectEffects(ItemStack stack, ImmutableList<TooltipEffect> associated) {
        final List<TooltipEffect> effects = new ArrayList<>(associated);
        if (ItemGroups.CURSED.test(stack)) categorizedEffect(effects, TooltipsRegistry.BUILTIN_EFFECT_ENCHANTMENT_CURSE.get());
        if (ItemGroups.ENCHANTED.test(stack)) categorizedEffect(effects, TooltipsRegistry.BUILTIN_EFFECT_ENCHANTMENT_GENERAL.get());
        if (ItemGroups.ENDER.test(stack)) categorizedEffect(effects, TooltipsRegistry.BUILTIN_EFFECT_ENDER.get());
        return effects;
    }

    private static void categorizedEffect(List<TooltipEffect> effects, TooltipEffect effect) {
        if (effects.stream().anyMatch(e -> e.category() == effect.category())) return;
        effects.add(effect);
    }

    private static String rarityName(ItemStack stack) {
        return stack.getRarity().toString().toUpperCase();
    }
}
