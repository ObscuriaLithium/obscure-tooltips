package com.obscuria.tooltips.client.style;

import com.google.common.collect.ImmutableList;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.TooltipIcon;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TooltipStylePreset {
    private final ImmutableList<TooltipEffect> EFFECTS;
    @Nullable private final TooltipPanel PANEL;
    @Nullable private final TooltipFrame FRAME;
    @Nullable private final TooltipIcon ICON;

    private TooltipStylePreset(List<TooltipEffect> effects, @Nullable TooltipPanel panel, @Nullable TooltipFrame frame, @Nullable TooltipIcon icon) {
        this.EFFECTS = ImmutableList.copyOf(effects);
        this.PANEL = panel;
        this.FRAME = frame;
        this.ICON = icon;
    }

    public Optional<TooltipPanel> getPanel() {
        return PANEL == null ? Optional.empty() : Optional.of(PANEL);
    }

    public Optional<TooltipFrame> getFrame() {
        return FRAME == null ? Optional.empty() : Optional.of(FRAME);
    }

    public Optional<TooltipIcon> getIcon() {
        return ICON == null ? Optional.empty() : Optional.of(ICON);
    }

    public ImmutableList<TooltipEffect> getEffects() {
        return EFFECTS;
    }

    @Override
    public String toString() {
        return "[Panel:%s, Frame:%s, Icon:%s, Effects:%s]".formatted(
                PANEL != null ? PANEL.getClass().getSimpleName() : "none",
                FRAME != null ? FRAME.getClass().getSimpleName() : "none",
                ICON != null ? ICON.getClass().getSimpleName() : "none",
                !EFFECTS.isEmpty() ? EFFECTS.stream().map(effect -> effect.getClass().getSimpleName()).toList() : "none");
    }

    public static class Builder {
        private final List<TooltipEffect> effects = new ArrayList<>();
        @Nullable private TooltipPanel panel;
        @Nullable private TooltipFrame frame;
        @Nullable private TooltipIcon icon;

        public Builder() {}

        public TooltipStylePreset.Builder withPanel(@Nullable TooltipPanel panel) {
            return this.withPanel(panel, true);
        }

        public TooltipStylePreset.Builder withFrame(@Nullable TooltipFrame frame) {
            return this.withFrame(frame, true);
        }

        public TooltipStylePreset.Builder withIcon(@Nullable TooltipIcon icon) {
            return this.withIcon(icon, true);
        }

        public TooltipStylePreset.Builder withPanel(@Nullable TooltipPanel panel, boolean override) {
            this.panel = this.panel == null || override ? panel : this.panel;
            return this;
        }

        public TooltipStylePreset.Builder withFrame(@Nullable TooltipFrame frame, boolean override) {
            this.frame = this.frame == null || override ? frame : this.frame;
            return this;
        }

        public TooltipStylePreset.Builder withIcon(@Nullable TooltipIcon icon, boolean override) {
            this.icon = this.icon == null || override ? icon : this.icon;
            return this;
        }

        public TooltipStylePreset.Builder withEffects(@Nullable List<TooltipEffect> effects) {
            if (effects == null) return this;
            testCategories: for (TooltipEffect effect: effects) {
                for (TooltipEffect loaded: this.effects)
                    if (!effect.canStackWith(loaded))
                        continue testCategories;
                this.effects.add(effect);
            }
            return this;
        }

        public boolean isEmpty() {
            return this.panel == null && this.frame == null && this.icon == null && this.effects.isEmpty();
        }

        public TooltipStylePreset build() {
            return new TooltipStylePreset(effects, panel, frame, icon);
        }
    }
}
