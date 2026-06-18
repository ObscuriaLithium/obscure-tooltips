package dev.obscuria.obscure_tooltips;

import dev.obscuria.obscure_tooltips.client.TooltipEventHandler;
import dev.obscuria.obscure_tooltips.client.registry.TooltipManager;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.12.2]",
        guiFactory = "dev.obscuria.obscure_tooltips.config.GuiFactory"
)
public final class ObscureTooltips {

    public static final Logger LOGGER = LogManager.getLogger(Tags.MOD_NAME);

    public static ResourceLocation resource(String path) {
        return new ResourceLocation(Tags.MOD_ID, path);
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Loading {}", Tags.MOD_NAME);

        final IResourceManager resourceManager = Minecraft.getMinecraft().getResourceManager();
        if (resourceManager instanceof IReloadableResourceManager) {
            ((IReloadableResourceManager) resourceManager).registerReloadListener(TooltipManager.SHARED);
        }
        TooltipManager.SHARED.onResourceManagerReload(resourceManager);

        MinecraftForge.EVENT_BUS.register(new TooltipEventHandler());
    }
}
