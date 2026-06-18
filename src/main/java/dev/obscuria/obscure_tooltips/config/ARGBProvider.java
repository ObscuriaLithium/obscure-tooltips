package dev.obscuria.obscure_tooltips.config;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import dev.obscuria.obscure_tooltips.util.color.ARGB;
import dev.obscuria.obscure_tooltips.util.color.Colors;

public interface ARGBProvider {

    ARGB get();

    static ARGBProvider fromJson(JsonElement json) {
        if (json.isJsonPrimitive() && json.getAsJsonPrimitive().isString()) {
            final String value = json.getAsString();
            if (value.startsWith("@")) {
                final ARGBDelegate delegate = ARGBDelegate.byName(value);
                if (delegate == null) {
                    throw new JsonParseException("Unknown color delegate: " + value);
                }
                return new Config(delegate);
            }
        }
        return new Literal(parseLiteral(json));
    }

    static ARGB parseLiteral(JsonElement json) {
        if (json.isJsonPrimitive()) {
            final JsonPrimitive primitive = json.getAsJsonPrimitive();
            if (primitive.isString()) {
                return Colors.argbOf(primitive.getAsString());
            }
            if (primitive.isNumber()) {
                return Colors.argbOf(primitive.getAsInt());
            }
        }
        if (json.isJsonArray()) {
            final JsonArray array = json.getAsJsonArray();
            if (array.size() == 4) {
                return Colors.argbOf(
                        array.get(0).getAsFloat(), array.get(1).getAsFloat(),
                        array.get(2).getAsFloat(), array.get(3).getAsFloat());
            }
        }
        throw new JsonParseException("Invalid color: " + json);
    }

    @Desugar
    record Literal(ARGB color) implements ARGBProvider {
        @Override
        public ARGB get() {
            return this.color;
        }
    }

    @Desugar
    record Config(ARGBDelegate delegate) implements ARGBProvider {
        @Override
        public ARGB get() {
            return this.delegate.color();
        }
    }
}
