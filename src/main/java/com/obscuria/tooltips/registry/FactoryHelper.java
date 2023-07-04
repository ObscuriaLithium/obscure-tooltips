package com.obscuria.tooltips.registry;

import com.google.gson.JsonObject;

public final class FactoryHelper {
    public static int color(JsonObject params, String name) {
        return (int) Long.parseLong(params.get(name).getAsString(), 16);
    }
}