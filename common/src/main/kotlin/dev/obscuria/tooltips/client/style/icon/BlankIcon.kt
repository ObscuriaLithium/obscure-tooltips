package dev.obscuria.tooltips.client.style.icon

import com.mojang.serialization.Codec
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack

object BlankIcon : ITooltipIcon {

    val CODEC: Codec<BlankIcon> = Codec.unit(BlankIcon)

    override fun codec(): Codec<out ITooltipIcon> = CODEC

    override fun render(graphics: GuiGraphics, stack: ItemStack, x: Int, y: Int) {}
}