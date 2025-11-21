package dev.obscuria.tooltips.forge;

import dev.obscuria.tooltips.ObscureTooltips;
import dev.obscuria.tooltips.client.BlacklistedItems;
import dev.obscuria.tooltips.client.registry.TooltipManager;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(ObscureTooltips.MOD_ID)
public class ForgeObscureTooltips {

    public ForgeObscureTooltips() {
        if (FMLEnvironment.dist.isDedicatedServer()) return;

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ForgeObscureTooltips::onLoadComplete);

        ObscureTooltips.init();
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(TooltipManager.INSTANCE);
    }

    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        BlacklistedItems.init();
    }
}