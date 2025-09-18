package dev.obscuria.tooltips.client.filter

import com.mojang.brigadier.exceptions.CommandSyntaxException
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.TagParser
import net.minecraft.world.item.ItemStack

class NbtFilter(
    val nbt: CompoundTag
) : IItemFilter {

    override fun codec(): Codec<NbtFilter> = CODEC

    override fun test(stack: ItemStack): Boolean {
        return NbtUtils.compareNbt(nbt, stack.tag, false)
    }

    companion object {

        private val TAG_CODEC: Codec<CompoundTag> = Codec.STRING.comapFlatMap(::stringToTag, ::tagToString)
        val CODEC: Codec<NbtFilter> = RecordCodecBuilder.create { codec ->
            codec.group(
                TAG_CODEC
                    .fieldOf("nbt")
                    .forGetter(NbtFilter::nbt)
            ).apply(codec, ::NbtFilter)
        }

        private fun stringToTag(input: String): DataResult<CompoundTag> = try {
            DataResult.success(TagParser.parseTag(input))
        } catch (e: CommandSyntaxException) {
            DataResult.error { "Invalid tag: ${e.message}" }
        }

        private fun tagToString(tag: CompoundTag): String {
            return tag.toString()
        }
    }
}