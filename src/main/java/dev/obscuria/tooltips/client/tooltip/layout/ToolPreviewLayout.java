package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.SplitComponent;
import dev.obscuria.tooltips.client.component.ToolPreviewComponent;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.util.Collections;
import java.util.List;

public final class ToolPreviewLayout extends AbstractHeaderLayout<ToolPreviewLayout.State> {

    public static final ToolPreviewLayout INSTANCE = new ToolPreviewLayout();

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<TooltipComponent> processPostWrap(State state, List<TooltipComponent> components, FontRenderer font) {
        return Collections.singletonList(new SplitComponent(new ToolPreviewComponent(state.stack), components));
    }

    public static final class State extends TooltipState {
        State(ItemStack stack) {
            super(stack);
        }
    }
}
