package com.obscuria.tooltips;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(ObscureTooltips.MODID)
public class ObscureTooltips {

    public static final String MODID = "obscure_tooltips";
    public static final Logger LOGGER = LogManager.getLogger("ObscureTooltips");

    public ObscureTooltips() {
        ObscureTooltipsConfig.setup();
        if (FMLEnvironment.dist == Dist.CLIENT)
            ObscureTooltipsClient.setup();
    }
}
