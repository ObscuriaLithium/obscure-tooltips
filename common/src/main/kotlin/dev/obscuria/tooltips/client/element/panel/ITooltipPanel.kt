package dev.obscuria.tooltips.client.element.panel

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipPanel {

    fun codec(): Codec<out ITooltipPanel>

    fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int)

    companion object {

        val CODEC: Codec<ITooltipPanel> = TooltipsRegistries.Resource.TOOLTIP_PANEL.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipPanel> = TooltipsRegistries.TOOLTIP_PANEL_TYPE.byNameCodec.dispatch(ITooltipPanel::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipPanel>) -> Any) {

            registrar("blank", BlankPanel::CODEC)
            registrar("color_rect", ColorRectPanel::CODEC)
        }
    }
}