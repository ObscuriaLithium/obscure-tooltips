package dev.obscuria.tooltips.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.obscuria.fragmentum.content.world.tooltip.GroupTooltip;
import dev.obscuria.tooltips.client.component.StackBuffer;
import dev.obscuria.tooltips.config.ClientConfig;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(value = ItemStack.class, priority = Integer.MAX_VALUE)
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public abstract class MixinItemStack {

    @ModifyReturnValue(method = "getTooltipImage", at = @At("RETURN"))
    private Optional<TooltipComponent> aquamirae$injectBuffer(Optional<TooltipComponent> original) {
        final var self = (ItemStack) (Object) this;
        if (!ClientConfig.ENABLED.get()) return original;
        if (ClientConfig.isIgnored(self.getItem())) return original;
        return Optional.of(GroupTooltip.maybeGroup(original.orElse(null), new StackBuffer(self)));
    }
}
