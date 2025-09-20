package dev.obscuria.tooltips.client.label

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class TranslatableLabelProvider(
    val key: String
) : ILabelProvider {

    override fun codec(): Codec<TranslatableLabelProvider> = CODEC

    override fun bake(stack: ItemStack): ClientTooltipComponent {
        val component = Component.translatable(key).withStyle(ChatFormatting.GRAY)
        return ClientTooltipComponent.create(component.visualOrderText)
    }

    companion object {

        val CODEC: Codec<TranslatableLabelProvider> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.STRING
                    .fieldOf("key")
                    .forGetter(TranslatableLabelProvider::key)
            ).apply(codec, ::TranslatableLabelProvider)
        }
    }
}