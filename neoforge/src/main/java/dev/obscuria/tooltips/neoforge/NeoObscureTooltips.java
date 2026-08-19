package dev.obscuria.tooltips.neoforge;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.registry.TooltipManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(ObscureTooltips.MODID)
public final class NeoObscureTooltips {

    public NeoObscureTooltips(IEventBus eventBus) {
        if (FMLEnvironment.dist.isDedicatedServer()) return;
        ObscureTooltips.init();
        Minecraft mc = Minecraft.getInstance();
        if (mc == null) return;
        if (mc.getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(TooltipManager.INSTANCE);
    }
}