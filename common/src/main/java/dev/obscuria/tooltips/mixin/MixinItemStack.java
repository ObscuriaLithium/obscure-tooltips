package dev.obscuria.tooltips.mixin;

import dev.obscuria.fragmentum.world.tooltip.GroupTooltip;
import dev.obscuria.tooltips.client.BlacklistedItems;
import dev.obscuria.tooltips.client.StackBuffer;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = ItemStack.class, priority = Integer.MAX_VALUE)
public abstract class MixinItemStack {

    @Inject(method = "getTooltipImage", at = @At("RETURN"), cancellable = true)
    private void injectStackBuffer(CallbackInfoReturnable<Optional<TooltipComponent>> info) {
        final var self = (ItemStack) (Object) this;
        if (!BlacklistedItems.isBlacklisted(self.getItem())) {
            final @Nullable var image = info.getReturnValue().orElse(null);
            final var group = GroupTooltip.maybeGroup(image, new StackBuffer(self));
            info.setReturnValue(Optional.of(group));
        }
    }
}
