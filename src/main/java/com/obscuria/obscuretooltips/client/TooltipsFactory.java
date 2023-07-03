package com.obscuria.obscuretooltips.client;

import com.google.gson.JsonObject;
import com.obscuria.obscuretooltips.ObscureTooltips;
import com.obscuria.obscuretooltips.client.style.effect.TooltipEffect;
import com.obscuria.obscuretooltips.client.style.frame.TextureFrame;
import com.obscuria.obscuretooltips.client.style.frame.TooltipFrame;
import com.obscuria.obscuretooltips.client.style.icon.TooltipIcon;
import com.obscuria.obscuretooltips.client.style.panel.ColorRectPanel;
import com.obscuria.obscuretooltips.client.style.panel.TooltipPanel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public final class TooltipsFactory {
    private static final HashMap<ResourceLocation, Function<JsonObject, TooltipPanel>> PANELS = new HashMap<>();
    private static final HashMap<ResourceLocation, Function<JsonObject, TooltipFrame>> FRAMES = new HashMap<>();
    private static final HashMap<ResourceLocation, Function<JsonObject, TooltipIcon>> ICONS = new HashMap<>();
    private static final HashMap<ResourceLocation, Function<JsonObject, TooltipEffect>> EFFECTS = new HashMap<>();

    public static void registerPanel(ResourceLocation key, Function<JsonObject, TooltipPanel> factory) {
        PANELS.put(key, factory);
    }

    public static void registerFrame(ResourceLocation key, Function<JsonObject, TooltipFrame> factory) {
        FRAMES.put(key, factory);
    }

    public static void registerIcon(ResourceLocation key, Function<JsonObject, TooltipIcon> factory) {
        ICONS.put(key, factory);
    }

    public static void registerEffect(ResourceLocation key, Function<JsonObject, TooltipEffect> factory) {
        EFFECTS.put(key, factory);
    }

    public static Optional<TooltipPanel> buildPanel(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(PANELS.get(key).apply(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.warn("Failed to build custom Panel from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipFrame> buildFrame(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(FRAMES.get(key).apply(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.warn("Failed to build custom Frame from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipIcon> buildIcon(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(ICONS.get(key).apply(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.warn("Failed to build custom Icon from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipEffect> buildEffect(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(EFFECTS.get(key).apply(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.warn("Failed to build custom Effect from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private static ResourceLocation key(String key) {
        return new ResourceLocation(ObscureTooltips.MODID, key);
    }

    static {
        registerPanel(key("default"), params -> TooltipsRegistry.PANEL_DEFAULT);
        registerPanel(key("silver"), params -> TooltipsRegistry.PANEL_SILVER);
        registerPanel(key("golden"), params -> TooltipsRegistry.PANEL_GOLDEN);
        registerPanel(key("color_rect"), ColorRectPanel::build);

        registerFrame(key("default"), params -> TooltipsRegistry.FRAME_DEFAULT);
        registerFrame(key("silver"), params -> TooltipsRegistry.FRAME_SILVER);
        registerFrame(key("golden"), params -> TooltipsRegistry.FRAME_GOLDEN);
        registerFrame(key("bones"), params -> TooltipsRegistry.FRAME_BONES);
        registerFrame(key("texture"), TextureFrame::build);

        registerIcon(key("default"), params -> TooltipsRegistry.ICON_DEFAULT);
        registerIcon(key("animated"), params -> TooltipsRegistry.ICON_ANIMATED);
        registerIcon(key("epic_animated"), params -> TooltipsRegistry.ICON_ANIMATED_EPIC);

        registerEffect(key("enchantment"), params -> TooltipsRegistry.ENCHANTMENT_EFFECT);
        registerEffect(key("tails"), params -> TooltipsRegistry.TAILS_EFFECT);
    }

    public static void setup() {}
}
