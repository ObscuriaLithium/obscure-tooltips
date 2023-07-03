package com.obscuria.obscuretooltips.client.style;

import com.google.common.collect.ImmutableList;
import com.obscuria.obscuretooltips.client.TooltipsRegistry;
import com.obscuria.obscuretooltips.client.style.effect.TooltipEffect;
import com.obscuria.obscuretooltips.client.style.frame.TooltipFrame;
import com.obscuria.obscuretooltips.client.style.icon.TooltipIcon;
import com.obscuria.obscuretooltips.client.style.panel.TooltipPanel;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public final class TooltipStyle {
    private final ImmutableList<TooltipEffect> EFFECTS;
    private final TooltipPanel PANEL;
    private final TooltipFrame FRAME;
    private final TooltipIcon ICON;

    private TooltipStyle(List<TooltipEffect> effects, TooltipPanel panel, TooltipFrame frame, TooltipIcon icon) {
        EFFECTS = ImmutableList.copyOf(effects);
        PANEL = panel;
        FRAME = frame;
        ICON = icon;
    }

    public void renderBack(GuiGraphics context, Vec2 pos, Point size, float seconds, boolean slot) {
        PANEL.render(context, pos, size, seconds, slot);
    }

    public void renderFront(GuiGraphics context, ItemStack stack, Vec2 pos, Point size, float seconds) {
        context.pose().pushPose();
        context.pose().translate(0, 0, 400);
        renderEffects(Effects.Order.LAYER_3_TEXT$FRAME, context, pos, size, seconds);
        context.pose().popPose();

        FRAME.render(context, pos, size, seconds);

        context.pose().pushPose();
        context.pose().translate(0, 0, 0);
        renderEffects(Effects.Order.LAYER_4_FRAME$ICON, context, pos, size, seconds);
        context.pose().popPose();

        context.pose().pushPose();
        context.pose().translate(pos.x + 12, pos.y + 12, 500);
        context.pose().pushPose();
        ICON.render(context, stack, -8, -8, seconds);
        context.pose().popPose();
        context.pose().popPose();
    }

    public void renderEffects(Effects.Order order, GuiGraphics context, Vec2 pos, Point size, float seconds) {
        for (TooltipEffect effect: EFFECTS)
            if (effect.order().equals(order))
                effect.render(context, pos, size, seconds);
    }

    public void reset() {
        PANEL.reset();
        ICON.reset();
        FRAME.reset();
        EFFECTS.forEach(TooltipEffect::reset);
    }

    public static class Builder {
        private final List<TooltipEffect> effects = new ArrayList<>();
        private TooltipPanel panel = TooltipsRegistry.PANEL_DEFAULT;
        private TooltipFrame frame = TooltipsRegistry.FRAME_DEFAULT;
        private TooltipIcon icon = TooltipsRegistry.ICON_DEFAULT;

        public Builder() {}

        public TooltipStyle.Builder withPanel(TooltipPanel panel) {
            this.panel = panel;
            return this;
        }

        public TooltipStyle.Builder withFrame(TooltipFrame frame) {
            this.frame = frame;
            return this;
        }

        public TooltipStyle.Builder withIcon(TooltipIcon icon) {
            this.icon = icon;
            return this;
        }

        public TooltipStyle.Builder withEffects(List<TooltipEffect> effects) {
            this.effects.addAll(effects);
            return this;
        }

        public TooltipStyle build() {
            return new TooltipStyle(effects, panel, frame, icon);
        }
    }
}
