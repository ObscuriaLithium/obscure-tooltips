package dev.obscuria.tooltips.config;

import java.util.function.Supplier;

public final class ConfigValue<T> {

    private final Supplier<T> getter;

    ConfigValue(Supplier<T> getter) {
        this.getter = getter;
    }

    public T get() {
        return this.getter.get();
    }
}
