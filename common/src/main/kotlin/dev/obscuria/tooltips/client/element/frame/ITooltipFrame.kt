package dev.obscuria.tooltips.client.element.frame

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipFrame {

    fun codec(): Codec<out ITooltipFrame>

    fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int)

    companion object {

        val CODEC: Codec<ITooltipFrame> = TooltipsRegistries.Resource.TOOLTIP_FRAME.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipFrame> = TooltipsRegistries.TOOLTIP_FRAME_TYPE.byNameCodec.dispatch(ITooltipFrame::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipFrame>) -> Any) {

            registrar("blank", BlankFrame::CODEC)
            registrar("nine_sliced", NineSlicedFrame::CODEC)
        }
    }
}