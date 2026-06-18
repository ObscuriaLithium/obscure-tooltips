package dev.obscuria.obscure_tooltips.client.registry;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResourceRegistry<T> {
    protected final String name;
    protected final Map<ResourceLocation, T> keyToElement = new ConcurrentHashMap<>();
    protected final Map<T, ResourceLocation> elementToKey = new ConcurrentHashMap<>();

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

    public T get(ResourceLocation key) {
        return keyToElement.get(key);
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
        return "ResourceRegistry[" + this.name + "]";
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
