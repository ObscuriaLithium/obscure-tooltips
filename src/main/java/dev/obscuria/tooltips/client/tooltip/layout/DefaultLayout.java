package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class DefaultLayout extends AbstractHeaderLayout<DefaultLayout.State> {

    public static final DefaultLayout INSTANCE = new DefaultLayout();

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<TooltipComponent> processPostWrap(State state, List<TooltipComponent> components, FontRenderer font) {
        return components;
    }

    public static final class State extends TooltipState {
        State(ItemStack stack) {
            super(stack);
        }
    }
}
