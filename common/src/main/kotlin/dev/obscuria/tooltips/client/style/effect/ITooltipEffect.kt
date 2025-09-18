package dev.obscuria.tooltips.client.style.effect

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.ResourceRegistry
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipEffect {

    fun codec(): Codec<out ITooltipEffect>

    fun canApply(effect: List<ITooltipEffect>): Boolean

    fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int)

    companion object {

        val CODEC: Codec<ITooltipEffect> = ResourceRegistry.Effects.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipEffect> = TooltipsRegistries.TOOLTIP_EFFECT_TYPE.byNameCodec.dispatch(ITooltipEffect::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipEffect>) -> Any) {

            registrar("rim_light", RimLightEffect::CODEC)
        }
    }
}