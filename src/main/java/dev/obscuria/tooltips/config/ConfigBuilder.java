package dev.obscuria.tooltips.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

public final class ConfigBuilder {
    private static final String LANG_PREFIX = "obscure_tooltips.configuration.";

    private final Configuration config;
    private final Deque<String> categories = new ArrayDeque<>();
    private String comment = "";

    public ConfigBuilder(String fileName) {
        this.config = new Configuration(new File(Loader.instance().getConfigDir(), fileName));
    }

    public Configuration getConfiguration() {
        return config;
    }

    public ConfigBuilder comment(String... lines) {
        comment = String.join("\n", lines);
        return this;
    }

    public void push(String category) {
        categories.addLast(category);
        config.getCategory(category()).setLanguageKey(LANG_PREFIX + category);
    }

    public void pop() {
        categories.removeLast();
    }

    public ConfigValue<Boolean> defineBoolean(String key, boolean defaultValue) {
        final Property property = define(config.get(category(), key, defaultValue, takeComment()), key);
        return new ConfigValue<>(property::getBoolean);
    }

    public ConfigValue<Integer> defineInt(String key, int defaultValue, int min, int max) {
        final Property property = define(config.get(category(), key, defaultValue, takeComment(), min, max), key);
        return new ConfigValue<>(property::getInt);
    }

    public ConfigValue<Double> defineDouble(String key, double defaultValue, double min, double max) {
        final Property property = define(config.get(category(), key, defaultValue, takeComment(), min, max), key);
        return new ConfigValue<>(property::getDouble);
    }

    public ConfigValue<String> defineString(String key, String defaultValue) {
        final Property property = define(config.get(category(), key, defaultValue, takeComment()), key);
        return new ConfigValue<>(property::getString);
    }

    public ConfigValue<List<String>> defineList(String key, List<String> defaultValue, Supplier<String> example) {
        final Property property = define(config.get(category(), key, defaultValue.toArray(new String[0]), takeComment()), key);
        return new ConfigValue<>(() -> Arrays.asList(property.getStringList()));
    }

    private static Property define(Property property, String key) {
        property.setLanguageKey(LANG_PREFIX + key);
        return property;
    }

    public void buildClient() {
        if (config.hasChanged()) {
            config.save();
        }
    }

    private String category() {
        return categories.isEmpty()
                ? Configuration.CATEGORY_GENERAL
                : String.join(Configuration.CATEGORY_SPLITTER, categories);
    }

    private String takeComment() {
        final String taken = comment;
        comment = "";
        return taken;
    }
}
