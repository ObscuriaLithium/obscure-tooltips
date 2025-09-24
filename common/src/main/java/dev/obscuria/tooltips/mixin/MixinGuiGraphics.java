package dev.obscuria.tooltips.mixin;

import dev.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(GuiGraphics.class)
public abstract class MixinGuiGraphics
{
    @Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void renderCustomTooltip(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner positioner, CallbackInfo info) {
        var self = (GuiGraphics) (Object) this;
        if (TooltipRenderer.render(self, font, new ArrayList<>(components), mouseX, mouseY, positioner)) info.cancel();
    }
}
