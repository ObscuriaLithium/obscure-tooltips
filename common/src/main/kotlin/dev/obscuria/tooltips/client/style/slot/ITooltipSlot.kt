package dev.obscuria.tooltips.client.style.slot

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.ResourceRegistry
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import java.util.function.Function

interface ITooltipSlot {

    fun codec(): Codec<out ITooltipSlot>

    fun render(graphics: GuiGraphics, x: Int, y: Int, width: Int, height: Int)

    companion object {

        val CODEC: Codec<ITooltipSlot> = ResourceRegistry.Slots.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipSlot> = TooltipsRegistries.TOOLTIP_SLOT_TYPE.byNameCodec.dispatch(ITooltipSlot::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipSlot>) -> Any) {

            registrar("blank", BlankSlot::CODEC)
            registrar("color_rect", ColorRectSlot::CODEC)
        }
    }
}