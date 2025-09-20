package dev.obscuria.tooltips.client.label

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class LiteralLabelProvider(
    val text: String
) : ILabelProvider {

    override fun codec(): Codec<LiteralLabelProvider> = CODEC

    override fun bake(stack: ItemStack): ClientTooltipComponent {
        val component = Component.literal(text).withStyle(ChatFormatting.GRAY)
        return ClientTooltipComponent.create(component.visualOrderText)
    }

    companion object {

        val CODEC: Codec<LiteralLabelProvider> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.STRING
                    .fieldOf("text")
                    .forGetter(LiteralLabelProvider::text)
            ).apply(codec, ::LiteralLabelProvider)
        }
    }
}