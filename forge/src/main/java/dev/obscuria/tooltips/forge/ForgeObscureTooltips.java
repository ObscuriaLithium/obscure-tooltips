package dev.obscuria.tooltips.forge;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.registry.TooltipsManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(ObscureTooltips.MOD_ID)
public class ForgeObscureTooltips {

    public ForgeObscureTooltips() {
        if (FMLEnvironment.dist.isDedicatedServer()) return;
        ObscureTooltips.init();
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(TooltipsManager.INSTANCE);
    }
}