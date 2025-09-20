package dev.obscuria.tooltips.client.element.icon

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.renderer.TooltipContext
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipIcon {

    fun codec(): Codec<out ITooltipIcon>

    fun render(graphics: GuiGraphics, context: TooltipContext, x: Int, y: Int)

    companion object {

        val CODEC: Codec<ITooltipIcon> = TooltipsRegistries.Resource.TOOLTIP_ICON.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipIcon> = TooltipsRegistries.TOOLTIP_ICON_TYPE.byNameCodec.dispatch(ITooltipIcon::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipIcon>) -> Any) {

            registrar("blank", BlankIcon::CODEC)
            registrar("static", StaticIcon::CODEC)
            registrar("accent", AccentIcon::CODEC)
            registrar("accent_spin", AccentSpinIcon::CODEC)
            registrar("accent_burst", AccentBurstIcon::CODEC)
        }
    }
}