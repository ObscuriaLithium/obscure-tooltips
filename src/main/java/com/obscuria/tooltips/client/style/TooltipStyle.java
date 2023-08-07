package com.obscuria.tooltips.client.style;

import com.google.common.collect.ImmutableList;
import com.obscuria.tooltips.client.StyleManager;
import com.obscuria.tooltips.client.renderer.TooltipContext;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.TooltipIcon;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
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

    public void renderBack(TooltipContext renderer, Vec2 pos, Point size, boolean slot) {
        renderer.pose().pushPose();
        renderer.pose().translate(0, 0, -50);
        PANEL.render(renderer, pos, size, slot);
        renderer.pose().popPose();
    }

    public void renderFront(TooltipContext renderer, Vec2 pos, Point size) {
        renderEffects(Effects.Order.LAYER_3_TEXT$FRAME, renderer, pos, size);
        renderer.push(() -> {
            renderer.translate(0, 0, -50);
            FRAME.render(renderer, pos, size);
        });
        renderEffects(Effects.Order.LAYER_4_FRAME$ICON, renderer, pos, size);
        renderer.push(() -> {
            renderer.translate(pos.x + 12, pos.y + 12, 500);
            renderer.push(() -> ICON.render(renderer, -8, -8));
        });
    }

    public void renderEffects(Effects.Order order, TooltipContext renderer, Vec2 pos, Point size) {
        renderer.push(() -> {
            renderer.translate(0, 0, switch (order) {
                case LAYER_1_BACK -> 0;
                case LAYER_2_BACK$TEXT -> 100;
                case LAYER_3_TEXT$FRAME -> 400;
                case LAYER_4_FRAME$ICON -> 500;
                case LAYER_5_FRONT -> 1000;
            });
            for (TooltipEffect effect : EFFECTS)
                if (effect.order().equals(order))
                    effect.render(renderer, pos, size);
        });
    }

    public void reset() {
        PANEL.reset();
        ICON.reset();
        FRAME.reset();
        EFFECTS.forEach(TooltipEffect::reset);
    }

    public static class Builder {
        private final List<TooltipEffect> effects = new ArrayList<>();
        private TooltipPanel panel = StyleManager.DEFAULT_PANEL;
        private TooltipFrame frame = StyleManager.DEFAULT_FRAME;
        private TooltipIcon icon = StyleManager.DEFAULT_ICON;

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
