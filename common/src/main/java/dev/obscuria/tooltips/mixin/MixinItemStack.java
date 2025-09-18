package dev.obscuria.tooltips.mixin;

import dev.obscuria.tooltips.client.renderer.TooltipRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class MixinItemStack
{
    @Inject(method = "getTooltipLines", at = @At("HEAD"))
    private void cacheTooltipStack(Player player, TooltipFlag isAdvanced, CallbackInfoReturnable<List<Component>> info) {
        TooltipRenderer.INSTANCE.setTooltipStack((ItemStack) (Object) this);
    }
}
