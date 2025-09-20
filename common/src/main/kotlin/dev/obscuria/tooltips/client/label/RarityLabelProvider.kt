package dev.obscuria.tooltips.client.label

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack

class RarityLabelProvider(
    val drawBackground: Boolean
) : ILabelProvider {

    override fun codec(): Codec<RarityLabelProvider> = CODEC

    override fun bake(stack: ItemStack): ClientTooltipComponent {
        val component = Component
            .translatable("rarity.${stack.rarity.name.lowercase()}")
            .withStyle(ChatFormatting.GRAY)
        return ClientTooltipComponent.create(component.visualOrderText)
    }

    companion object {

        val CODEC: Codec<RarityLabelProvider> = RecordCodecBuilder.create { codec ->
            codec.group(
                Codec.BOOL
                    .optionalFieldOf("draw_background", false)
                    .forGetter(RarityLabelProvider::drawBackground)
            ).apply(codec, ::RarityLabelProvider)
        }
    }
}