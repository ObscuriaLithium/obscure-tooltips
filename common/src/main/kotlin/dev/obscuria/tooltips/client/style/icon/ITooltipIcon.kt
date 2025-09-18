package dev.obscuria.tooltips.client.style.icon

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.client.ResourceRegistry
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import java.util.function.Function

interface ITooltipIcon {

    fun codec(): Codec<out ITooltipIcon>

    fun render(graphics: GuiGraphics, stack: ItemStack, x: Int, y: Int)

    companion object {

        val CODEC: Codec<ITooltipIcon> = ResourceRegistry.Icons.byNameCodec()
        val DIRECT_CODEC: Codec<ITooltipIcon> = TooltipsRegistries.TOOLTIP_ICON_TYPE.byNameCodec.dispatch(ITooltipIcon::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ITooltipIcon>) -> Any) {

            registrar("blank", BlankIcon::CODEC)
            registrar("static", StaticIcon::CODEC)
        }
    }
}