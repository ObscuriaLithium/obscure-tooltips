package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface TooltipLayout<T extends TooltipState> {

    T extractState(ItemStack stack);

    List<TooltipComponent> processPreWrap(T state, List<TooltipComponent> components, FontRenderer font);

    List<TooltipComponent> processPostWrap(T state, List<TooltipComponent> components, FontRenderer font);

    default List<TooltipComponent> rawProcessPreWrap(TooltipState state, List<TooltipComponent> components, FontRenderer font) {
        return processPreWrap(adapt(state), components, font);
    }

    default List<TooltipComponent> rawProcessPostWrap(TooltipState state, List<TooltipComponent> components, FontRenderer font) {
        return processPostWrap(adapt(state), components, font);
    }

    @SuppressWarnings("unchecked")
    default T adapt(TooltipState state) {
        return (T) state;
    }
}
