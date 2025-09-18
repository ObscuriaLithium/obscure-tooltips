package dev.obscuria.tooltips.forge

import dev.obscuria.tooltips.ObscureTooltips
import net.minecraftforge.fml.common.Mod

@Mod(ObscureTooltips.MODID)
class ForgeModTemplate
{
    init
    {
        ObscureTooltips.init()
    }
}