package com.obscuria.obscuretooltips;

import com.obscuria.obscuretooltips.tooltips.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ObscureTooltipsMod.MODID)
public class ObscureTooltipsMod {
    public static final String MODID = "obscure_tooltips";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public ObscureTooltipsMod() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            MinecraftForge.EVENT_BUS.addListener(TooltipRenderer::onTick);
            MinecraftForge.EVENT_BUS.addListener(TooltipRenderer::onTooltip);
            if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager resourceManager)
                resourceManager.registerReloadListener(Resources.INSTANCE);
        }
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> "ANY", (remote, isServer) -> true));
    }
}
