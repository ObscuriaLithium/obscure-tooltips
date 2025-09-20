package dev.obscuria.tooltips.client.element.effect

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipEffect {

    fun codec(): Codec<out ITooltipEffect>

    fun canApply(effect: List<ITooltipEffect>): Boolean

    fun renderIcon(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int) {}

    fun renderBack(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int, width: Int, height: Int) {}

    fun renderFront(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int, width: Int, height: Int) {}

    companion object {

        val CODEC: Codec<ITooltipEffect> = TooltipsRegistries.Resource.TOOLTIP_EFFECT.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipEffect> = TooltipsRegistries.TOOLTIP_EFFECT_TYPE.byNameCodec.dispatch(ITooltipEffect::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipEffect>) -> Any) {

            registrar("rim_light", RimLightEffect::CODEC)
            registrar("ray_glow", RayGlowEffect::CODEC)
            registrar("inward_particle", InwardParticleEffect::CODEC)
            registrar("icon_particle", IconParticleEffect::CODEC)
        }
    }
}