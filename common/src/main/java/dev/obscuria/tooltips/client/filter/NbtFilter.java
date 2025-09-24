package dev.obscuria.tooltips.client.filter;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.TagParser;
import net.minecraft.world.item.ItemStack;

public record NbtFilter(CompoundTag nbt) implements ItemFilter {

    public static final Codec<NbtFilter> CODEC;

    @Override
    public Codec<NbtFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return NbtUtils.compareNbt(nbt, stack.getTag(), false);
    }

    private static DataResult<CompoundTag> tryParseNbt(String input) {
        try {
            return DataResult.success(TagParser.parseTag(input));
        } catch (CommandSyntaxException exception) {
            return DataResult.error(() -> "Invalid tag: " + exception.getMessage());
        }
    }

    static {
        final var tagCodec = Codec.STRING.comapFlatMap(NbtFilter::tryParseNbt, CompoundTag::toString);
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                tagCodec.fieldOf("nbt").forGetter(NbtFilter::nbt)
        ).apply(codec, NbtFilter::new));
    }
}
