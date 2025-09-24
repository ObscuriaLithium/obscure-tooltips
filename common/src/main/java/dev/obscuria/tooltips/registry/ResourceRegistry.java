package dev.obscuria.tooltips.registry;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class ResourceRegistry<T> {

    protected final String name;
    protected final Map<ResourceLocation, T> keyToElement = Maps.newConcurrentMap();
    protected final Map<T, ResourceLocation> elementToKey = Maps.newConcurrentMap();

    public ResourceRegistry(String name) {
        this.name = name;
    }

    public void register(ResourceLocation key, T element) {
        keyToElement.put(key, element);
        elementToKey.put(element, key);
    }

    public Collection<T> listElements() {
        return keyToElement.values();
    }

    public Codec<T> byNameCodec() {
        return ResourceLocation.CODEC.flatXmap(this::tryGetElement, this::tryGetKey);
    }

    public void onReloadStart() {
        keyToElement.clear();
        elementToKey.clear();
    }

    public void onReloadEnd() {}

    public int total() {
        return keyToElement.size();
    }

    @Override
    public String toString() {
        return "ResourceRegistry[" + name + "]";
    }

    private DataResult<T> tryGetElement(ResourceLocation key) {
        return Optional.ofNullable(keyToElement.get(key))
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry key in %s: %s".formatted(toString(), key)));
    }

    private DataResult<ResourceLocation> tryGetKey(T element) {
        return Optional.ofNullable(elementToKey.get(element))
                .map(DataResult::success)
                .orElseGet(() -> DataResult.error(() -> "Unknown registry element in %s: %s".formatted(toString(), element)));
    }

    public static class Ordered<T extends Comparable<T>> extends ResourceRegistry<T> {

        private final List<T> sortedElements = Lists.newArrayList();

        public Ordered(String name) {
            super(name);
        }

        @Override
        public Collection<T> listElements() {
            return sortedElements;
        }

        @Override
        public void onReloadEnd() {
            sortedElements.clear();
            sortedElements.addAll(keyToElement.values());
            sortedElements.sort(Comparator.reverseOrder());
        }
    }
}
