package dev.obscuria.tooltips.client.particle

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.client.renderer.TooltipParticleData
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipParticle {

    fun codec(): Codec<out ITooltipParticle>

    fun render(graphics: GuiGraphics, context: TooltipContext, data: TooltipParticleData)

    companion object {

        val CODEC: Codec<ITooltipParticle> = TooltipsRegistries.TOOLTIP_PARTICLE_TYPE.byNameCodec.dispatch(ITooltipParticle::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipParticle>) -> Any) {

            registrar("horizontal_line", HorizontalLineParticle::CODEC)
            registrar("texture", TextureParticle::CODEC)
        }
    }
}