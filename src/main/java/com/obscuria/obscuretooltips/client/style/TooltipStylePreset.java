package com.obscuria.obscuretooltips.client.style;

import com.google.common.collect.ImmutableList;
import com.obscuria.obscuretooltips.client.style.effect.TooltipEffect;
import com.obscuria.obscuretooltips.client.style.frame.TooltipFrame;
import com.obscuria.obscuretooltips.client.style.icon.TooltipIcon;
import com.obscuria.obscuretooltips.client.style.panel.TooltipPanel;
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

    public static class Builder {
        private final List<TooltipEffect> effects = new ArrayList<>();
        @Nullable private TooltipPanel panel;
        @Nullable private TooltipFrame frame;
        @Nullable private TooltipIcon icon;

        public Builder() {}

        public TooltipStylePreset.Builder withPanel(@Nullable TooltipPanel panel) {
            this.panel = panel;
            return this;
        }

        public TooltipStylePreset.Builder withFrame(@Nullable TooltipFrame frame) {
            this.frame = frame;
            return this;
        }

        public TooltipStylePreset.Builder withIcon(@Nullable TooltipIcon icon) {
            this.icon = icon;
            return this;
        }

        public TooltipStylePreset.Builder withEffects(List<TooltipEffect> effects) {
            this.effects.addAll(effects);
            return this;
        }

        public TooltipStylePreset build() {
            return new TooltipStylePreset(effects, panel, frame, icon);
        }
    }
}
