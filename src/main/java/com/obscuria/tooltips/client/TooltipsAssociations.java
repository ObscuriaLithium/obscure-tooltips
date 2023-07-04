package com.obscuria.tooltips.client;

import com.google.common.collect.ImmutableList;
import com.obscuria.tooltips.client.style.Effects;
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
import java.util.function.Predicate;

public class TooltipsAssociations {
    private static final HashMap<Item, TooltipStylePreset> ASSOCIATIONS = new HashMap<>();
    private static final List<Preset<TooltipPanel>> PRESET_PANELS = new ArrayList<>();
    private static final List<Preset<TooltipFrame>> PRESET_FRAMES = new ArrayList<>();
    private static final List<Preset<TooltipIcon>> PRESET_ICONS = new ArrayList<>();
    private static final List<Preset<TooltipEffect>> PRESET_EFFECTS = new ArrayList<>();

    public static void associate(Item item, TooltipStylePreset preset) {
        ASSOCIATIONS.put(item, preset);
    }

    public static void presetPanel(byte priority, TooltipPanel panel, Predicate<ItemStack> condition) {
        PRESET_PANELS.add(new Preset<>(priority, panel, condition));
    }

    public static void presetFrame(byte priority, TooltipFrame frame, Predicate<ItemStack> condition) {
        PRESET_FRAMES.add(new Preset<>(priority, frame, condition));
    }

    public static void presetIcon(byte priority, TooltipIcon icon, Predicate<ItemStack> condition) {
        PRESET_ICONS.add(new Preset<>(priority, icon, condition));
    }

    public static void presetEffect(byte priority, TooltipEffect effect, Predicate<ItemStack> condition) {
        PRESET_EFFECTS.add(new Preset<>(priority, effect, condition));
    }

    public static Optional<TooltipStyle> styleFor(ItemStack stack) {
        final TooltipStylePreset preset = ResourceLoader.getStyleFor(stack.getItem())
                .orElse(ASSOCIATIONS.get(stack.getItem()));
        if (preset == null) return defaultStyle(stack);
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(preset.getPanel().orElse(defaultPanel(stack)))
                .withFrame(preset.getFrame().orElse(defaultFrame(stack)))
                .withIcon(preset.getIcon().orElse(defaultIcon(stack)))
                .withEffects(defaultEffects(stack, preset.getEffects()))
                .build());
    }

    @Contract("_ -> new")
    public static Optional<TooltipStyle> defaultStyle(ItemStack stack) {
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(defaultPanel(stack))
                .withFrame(defaultFrame(stack))
                .withIcon(defaultIcon(stack))
                .withEffects(defaultEffects(stack, ImmutableList.of()))
                .build());
    }

    public static TooltipPanel defaultPanel(ItemStack stack) {
        return searchPreset(new ArrayList<>(PRESET_PANELS), stack)
                .map(tooltipPanelPreset -> tooltipPanelPreset.ELEMENT)
                .orElse(TooltipsRegistry.BUILTIN_PANEL_DEFAULT.get());
    }

    public static TooltipFrame defaultFrame(ItemStack stack) {
        return searchPreset(new ArrayList<>(PRESET_FRAMES), stack)
                .map(tooltipPanelPreset -> tooltipPanelPreset.ELEMENT)
                .orElse(TooltipsRegistry.BUILTIN_FRAME_BLANK.get());
    }

    public static TooltipIcon defaultIcon(ItemStack stack) {
        return searchPreset(new ArrayList<>(PRESET_ICONS), stack)
                .map(tooltipPanelPreset -> tooltipPanelPreset.ELEMENT)
                .orElse(TooltipsRegistry.BUILTIN_ICON_COMMON.get());
    }

    public static List<TooltipEffect> defaultEffects(ItemStack stack, ImmutableList<TooltipEffect> associated) {
        final List<TooltipEffect> effects = new ArrayList<>(associated);
        searchAllPresets(new ArrayList<>(PRESET_EFFECTS), stack).stream().map(preset -> preset.ELEMENT)
                .forEach(effect -> {
                    if (effect.category() == Effects.Category.NONE
                            || effects.stream().noneMatch(e -> e.category() == effect.category()))
                        effects.add(effect);
                });
        return effects;
    }

    private static <T> Optional<Preset<T>> searchPreset(List<Preset<T>> presets, ItemStack stack) {
        for (byte i = 0; i < Byte.MAX_VALUE; i++) {
            final byte priority = i;
            final List<Preset<T>> list1 = presets.stream().filter(preset -> preset.PRIORITY == priority).toList();
            final List<Preset<T>> list2 = list1.stream().filter(preset -> preset.CONDITION.test(stack)).toList();
            if (!list2.isEmpty()) return Optional.of(list2.get(0));
            presets.removeAll(list1);
            if (presets.isEmpty()) return Optional.empty();
        }
        return Optional.empty();
    }

    private static <T> List<Preset<T>> searchAllPresets(List<Preset<T>> presets, ItemStack stack) {
        final List<Preset<T>> result = new ArrayList<>();
        for (byte i = 0; i < Byte.MAX_VALUE; i++) {
            final byte priority = i;
            final List<Preset<T>> list = presets.stream().filter(preset -> preset.PRIORITY == priority).toList();
            result.addAll(list.stream().filter(preset -> preset.CONDITION.test(stack)).toList());
            presets.removeAll(list);
            if (presets.isEmpty()) return result;
        }
        return result;
    }

    static {
        presetPanel((byte)60, TooltipsRegistry.BUILTIN_PANEL_SILVER.get(), ItemGroups.UNCOMMON_RARE);
        presetPanel((byte)60, TooltipsRegistry.BUILTIN_PANEL_GOLDEN.get(), ItemGroups.EPIC_LEGENDARY_MYTHIC);
        presetFrame((byte)60, TooltipsRegistry.BUILTIN_FRAME_SILVER.get(), ItemGroups.RARE);
        presetFrame((byte)60, TooltipsRegistry.BUILTIN_FRAME_GOLDEN.get(), ItemGroups.EPIC_LEGENDARY_MYTHIC);
        presetIcon((byte)60, TooltipsRegistry.BUILTIN_ICON_RARE.get(), ItemGroups.UNCOMMON_RARE);
        presetIcon((byte)60, TooltipsRegistry.BUILTIN_ICON_EPIC.get(), ItemGroups.EPIC_LEGENDARY_MYTHIC);
        presetEffect((byte)60, TooltipsRegistry.BUILTIN_EFFECT_ENCHANTMENT_CURSE.get(), ItemGroups.CURSED);
        presetEffect((byte)62, TooltipsRegistry.BUILTIN_EFFECT_ENCHANTMENT_GENERAL.get(), ItemGroups.ENCHANTED);
        presetEffect((byte)60, TooltipsRegistry.BUILTIN_EFFECT_ENDER.get(), ItemGroups.ENDER);
    }

    public static void setup() {}

    private static class Preset<T> {
        private final Predicate<ItemStack> CONDITION;
        private final T ELEMENT;
        private final byte PRIORITY;

        private Preset(byte priority, T element, Predicate<ItemStack> condition) {
            this.CONDITION = condition;
            this.ELEMENT = element;
            this.PRIORITY = (byte) Math.max(0, priority);
        }
    }
}
