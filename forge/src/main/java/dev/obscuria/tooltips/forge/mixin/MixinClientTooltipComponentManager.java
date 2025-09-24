package dev.obscuria.tooltips.forge.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraftforge.client.gui.ClientTooltipComponentManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ClientTooltipComponentManager.class, remap = false)
public abstract class MixinClientTooltipComponentManager {

    @Inject(method = "createClientTooltipComponent", at = @At("RETURN"), cancellable = true)
    private static void createCustomComponent(TooltipComponent component, CallbackInfoReturnable<ClientTooltipComponent> info) {
        //TooltipComponentRegistry.create(component).ifPresent(info::setReturnValue);
    }
}
