package com.obscuria.tooltips.client;

import com.obscuria.tooltips.client.style.TooltipStyle;
import com.obscuria.tooltips.client.style.TooltipStylePreset;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.DescentSimpleIcon;
import com.obscuria.tooltips.client.style.icon.TooltipIcon;
import com.obscuria.tooltips.client.style.panel.ColorRectPanel;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class StyleManager {
    public static final TooltipPanel DEFAULT_PANEL = new ColorRectPanel(0xf0100010, 0xf0100010, 0x505000ff, 0x5028007f, 0x20ffffff);
    public static final TooltipFrame DEFAULT_FRAME = (renderer, pos, size) -> {};
    public static final TooltipIcon DEFAULT_ICON = new DescentSimpleIcon();

    public static Optional<TooltipStyle> getStyleFor(ItemStack stack) {
        final TooltipStylePreset preset = ResourceLoader.getStyleFor(stack).orElse(null);
        if (preset == null) return defaultStyle();
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(preset.getPanel().orElse(DEFAULT_PANEL))
                .withFrame(preset.getFrame().orElse(DEFAULT_FRAME))
                .withIcon(preset.getIcon().orElse(DEFAULT_ICON))
                .withEffects(preset.getEffects())
                .build());
    }

    public static Optional<TooltipStyle> defaultStyle() {
        return Optional.of(new TooltipStyle.Builder()
                .withPanel(DEFAULT_PANEL)
                .withFrame(DEFAULT_FRAME)
                .withIcon(DEFAULT_ICON)
                .build());
    }
}
