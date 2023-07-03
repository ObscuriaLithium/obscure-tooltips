package com.obscuria.obscuretooltips.mixin;

import com.obscuria.obscuretooltips.client.renderer.TooltipRenderer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiGraphics.class, priority = 0)
public abstract class GuiGraphicsMixin {

    @Shadow private ItemStack tooltipStack;

    @Inject(method = "renderTooltipInternal", at = @At("HEAD"), cancellable = true)
    private void renderTooltip(Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner, CallbackInfo ci) {
        if (TooltipRenderer.render((GuiGraphics) ((Object)this), this.tooltipStack, font, components, x, y, positioner)) ci.cancel();
    }
}
