package dev.obscuria.tooltips.client.filter;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.ExtraCodecs.TagOrElementLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.stream.Stream;

public record ItemOrTagFilter(
        List<TagKey<Item>> tags,
        List<Item> items
) implements ItemFilter {

    public static final Codec<ItemOrTagFilter> CODEC;

    @Override
    public Codec<ItemOrTagFilter> codec() {
        return CODEC;
    }

    @Override
    public boolean test(ItemStack stack) {
        return items.contains(stack.getItem()) || tags.stream().anyMatch(stack::is);
    }

    private List<TagOrElementLocation> pack() {
        return Stream.concat(
                tags.stream().map(ItemOrTagFilter::packTag),
                items.stream().map(ItemOrTagFilter::packItem)).toList();
    }

    private static TagOrElementLocation packTag(TagKey<Item> tag) {
        return new TagOrElementLocation(tag.location(), true);
    }

    @SuppressWarnings("deprecation")
    private static TagOrElementLocation packItem(Item item) {
        return new TagOrElementLocation(item.builtInRegistryHolder().key().location(), false);
    }

    private static ItemOrTagFilter unpack(List<TagOrElementLocation> packed) {
        final var tags = Lists.<TagKey<Item>>newArrayList();
        final var items = Lists.<Item>newArrayList();
        for (var location : packed) {
            if (location.tag()) tags.add(TagKey.create(Registries.ITEM, location.id()));
            else items.add(BuiltInRegistries.ITEM.get(location.id()));
        }
        return new ItemOrTagFilter(tags, items);
    }

    static {
        CODEC = RecordCodecBuilder.create(codec -> codec.group(
                ExtraCodecs.TAG_OR_ELEMENT_ID.listOf().fieldOf("values").forGetter(ItemOrTagFilter::pack)
        ).apply(codec, ItemOrTagFilter::unpack));
    }
}
