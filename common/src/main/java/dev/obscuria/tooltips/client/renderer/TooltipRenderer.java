package dev.obscuria.tooltips.client.renderer;

import dev.obscuria.tooltips.client.TooltipDefinition;
import dev.obscuria.tooltips.client.TooltipLabel;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class TooltipRenderer {

    private static ItemStack lastStack = ItemStack.EMPTY;
    private static ItemStack actualStack = ItemStack.EMPTY;
    private static TooltipContext context = new TooltipContext();

    public static void perform(ItemStack stack) {
        actualStack = stack;
        if (ItemStack.isSameItemSameTags(lastStack, actualStack)) return;
        context = new TooltipContext(
                actualStack,
                TooltipDefinition.aggregateStyleFor(stack),
                TooltipLabel.findFor(stack));
    }

    public static boolean render(
            GuiGraphics graphics, Font font, List<ClientTooltipComponent> components,
            int mouseX, int mouseY, ClientTooltipPositioner positioner) {

        if (actualStack.isEmpty()) return false;
        if (components.isEmpty()) return false;

        final var title = components.remove(0);
        final var label = context.label() != null
                ? context.label().create(actualStack)
                : BlankComponent.INSTANCE;
        final var minWidth = widthOf(components, font);
        components.add(0, new HeaderComponent(minWidth, !components.isEmpty(), context, title, label));

        final var width = widthOf(components, font);
        final var height = heightOf(components) - 2;
        final var pos = positioner.positionTooltip(graphics.guiWidth(), graphics.guiHeight(), mouseX, mouseY, width, height);

        graphics.pose().pushPose();
        graphics.pose().translate(0f, 0f, 400f);

        graphics.flush();
        context.style().panel().ifPresent(it -> it.render(graphics, pos.x(), pos.y(), width, height));
        context.style().effects().forEach(it -> it.renderBack(graphics, context, pos.x(), pos.y(), width, height));
        graphics.pose().pushPose();
        graphics.pose().translate(0f, 0f, 2f);
        context.style().frame().ifPresent(it -> it.render(graphics, pos.x(), pos.y(), width, height));
        graphics.pose().popPose();
        graphics.flush();

        var componentY = pos.y();
        for (var component : components) {
            component.renderText(font, pos.x(), componentY, graphics.pose().last().pose(), graphics.bufferSource());
            component.renderImage(font, pos.x(), componentY, graphics);
            componentY += component.getHeight();
        }

        graphics.pose().popPose();

        lastStack = actualStack;
        actualStack = ItemStack.EMPTY;
        context.removeExpiredParticles();
        return true;
    }

    private static int widthOf(List<ClientTooltipComponent> components, Font font) {
        int max = 0;
        for (var component : components) {
            final var width = component.getWidth(font);
            if (width <= max) continue;
            max = width;
        }
        return max;
    }

    private static int heightOf(List<ClientTooltipComponent> components) {
        int sum = 0;
        for (var component : components) {
            sum += component.getHeight();
        }
        return sum;
    }
}
