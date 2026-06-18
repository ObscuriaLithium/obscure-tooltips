package dev.obscuria.tooltips.client;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import dev.obscuria.tooltips.client.render.GuiGraphics;
import dev.obscuria.tooltips.client.tooltip.TooltipScroll;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

public final class TooltipEventHandler {
    private final GuiGraphics graphics = new GuiGraphics(Minecraft.getMinecraft());

    @SubscribeEvent
    public void onRenderTooltipPre(RenderTooltipEvent.Pre event) {
        final ItemStack stack = event.getStack();
        if (stack.isEmpty()) {
            return;
        }

        if (ClientConfig.isIgnored(stack.getItem())) {
            return;
        }

        try {
            final List<TooltipComponent> components = new ArrayList<>(event.getLines().size());
            for (String line : event.getLines()) {
                components.add(TooltipComponent.create(line));
            }
            final boolean rendered = TooltipRenderer.render(graphics, event.getFontRenderer(), components,
                    event.getX(), event.getY(), event.getScreenWidth(), event.getScreenHeight(), stack);
            if (rendered) {
                event.setCanceled(true);
            }
        } catch (Exception exception) {
            ObscureTooltips.LOGGER.error("Failed to render styled tooltip for {}: {}", stack, exception.toString());
        }
    }

    @SubscribeEvent
    public void onGuiMouseInput(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!TooltipScroll.shouldCaptureInput()) {
            return;
        }
        final int dWheel = Mouse.getEventDWheel();
        if (dWheel != 0) {
            TooltipScroll.onInput(Integer.signum(dWheel));
            event.setCanceled(true);
        }
    }
}
