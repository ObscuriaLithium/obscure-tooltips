package dev.obscuria.tooltips

import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.resources.ResourceLocation
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ObscureTooltips {

    const val MODID = "obscure_tooltips"
    const val DISPLAY_NAME = "Obscure Tooltips"
    val LOGGER: Logger = LoggerFactory.getLogger(DISPLAY_NAME)

    fun key(name: String): ResourceLocation {
        return ResourceLocation(MODID, name)
    }

    fun init() {
        TooltipsRegistries.init()
    }
}