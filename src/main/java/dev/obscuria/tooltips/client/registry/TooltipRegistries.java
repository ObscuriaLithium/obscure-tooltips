package dev.obscuria.tooltips.client.registry;

import dev.obscuria.tooltips.client.tooltip.TooltipDefinition;
import dev.obscuria.tooltips.client.tooltip.TooltipLabel;
import dev.obscuria.tooltips.client.tooltip.TooltipStyle;
import dev.obscuria.tooltips.client.tooltip.element.effect.TooltipEffect;
import dev.obscuria.tooltips.client.tooltip.element.frame.TooltipFrame;
import dev.obscuria.tooltips.client.tooltip.element.icon.TooltipIcon;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import dev.obscuria.tooltips.client.tooltip.element.slot.TooltipSlot;

public final class TooltipRegistries {
    public static final ResourceRegistry<TooltipPanel> TOOLTIP_PANEL = new ResourceRegistry<>("panel");
    public static final ResourceRegistry<TooltipFrame> TOOLTIP_FRAME = new ResourceRegistry<>("frame");
    public static final ResourceRegistry<TooltipSlot> TOOLTIP_SLOT = new ResourceRegistry<>("slot");
    public static final ResourceRegistry<TooltipIcon> TOOLTIP_ICON = new ResourceRegistry<>("icon");
    public static final ResourceRegistry<TooltipEffect> TOOLTIP_EFFECT = new ResourceRegistry<>("effect");
    public static final ResourceRegistry<TooltipStyle> TOOLTIP_STYLE = new ResourceRegistry<>("style");
    public static final ResourceRegistry.Ordered<TooltipDefinition> TOOLTIP_DEFINITION = new ResourceRegistry.Ordered<>("definition");
    public static final ResourceRegistry.Ordered<TooltipLabel> TOOLTIP_LABEL = new ResourceRegistry.Ordered<>("label");
}
