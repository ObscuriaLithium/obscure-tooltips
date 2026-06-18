package dev.obscuria.obscure_tooltips.mixin;

import dev.obscuria.obscure_tooltips.client.registry.VibrantTooltipsPack;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ResourcePackRepository.class)
public abstract class MixinResourcePackRepository {

    @Shadow
    private List<ResourcePackRepository.Entry> repositoryEntriesAll;

    @Inject(method = "updateRepositoryEntriesAll", at = @At("TAIL"))
    private void obscure_tooltips$injectBuiltInPacks(CallbackInfo ci) {
        VibrantTooltipsPack.injectInto((ResourcePackRepository) (Object) this, this.repositoryEntriesAll);
    }
}
