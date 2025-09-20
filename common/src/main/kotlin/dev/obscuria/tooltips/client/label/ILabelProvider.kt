package dev.obscuria.tooltips.client.label

import com.mojang.serialization.Codec
import dev.obscuria.tooltips.registry.TooltipsRegistries
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.world.item.ItemStack
import java.util.function.Function

interface ILabelProvider {

    fun codec(): Codec<out ILabelProvider>

    fun bake(stack: ItemStack): ClientTooltipComponent

    companion object {

        val CODEC: Codec<ILabelProvider> = TooltipsRegistries.LABEL_PROVIDER_TYPE.byNameCodec.dispatch(ILabelProvider::codec, Function.identity())

        internal fun bootstrap(registrar: (String, () -> Codec<out ILabelProvider>) -> Any) {

            registrar("rarity", RarityLabelProvider::CODEC)
            registrar("literal", LiteralLabelProvider::CODEC)
            registrar("translatable", TranslatableLabelProvider::CODEC)
        }
    }
}