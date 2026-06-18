package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.HeaderComponent;
import dev.obscuria.tooltips.client.component.TextComponent;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import dev.obscuria.tooltips.client.tooltip.element.panel.TooltipPanel;
import dev.obscuria.tooltips.util.color.ARGB;
import net.minecraft.client.gui.FontRenderer;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractHeaderLayout<T extends TooltipState> implements TooltipLayout<T> {

    @Override
    public List<TooltipComponent> processPreWrap(T state, List<TooltipComponent> components, FontRenderer font) {
        return this.makeHeader(state, components, font);
    }

    protected List<TooltipComponent> makeHeader(T state, List<TooltipComponent> components, FontRenderer font) {
        final @Nullable var title = extractFirstText(components);
        if (title == null) return components;
        final var label = state.createLabel();
        components.add(0, new HeaderComponent(state, title, label,
                shouldDrawSeparator(components),
                pickSeparatorColor(state)));
        return components;
    }

    private @Nullable TooltipComponent extractFirstText(List<TooltipComponent> components) {
        for (var i = 0; i < components.size(); i++) {
            if (!(components.get(i) instanceof TextComponent)) continue;
            return components.remove(i);
        }
        return null;
    }

    private boolean isZeroHeight(List<TooltipComponent> components) {
        return components.stream().mapToInt(TooltipComponent::getHeight).sum() <= 0;
    }

    private boolean shouldDrawSeparator(List<TooltipComponent> components) {
        return !components.isEmpty() && !isZeroHeight(components);
    }

    private ARGB pickSeparatorColor(T state) {
        return state.style.panel()
                .map(TooltipPanel::separatorColor)
                .orElse(TooltipPanel.DEFAULT_SEPARATOR_COLOR);
    }
}
