package com.obscuria.tooltips.registry;

import com.google.gson.JsonObject;
import com.obscuria.tooltips.ObscureTooltips;
import com.obscuria.tooltips.client.style.effect.EnchantmentEffect;
import com.obscuria.tooltips.client.style.effect.EnderEffect;
import com.obscuria.tooltips.client.style.effect.TooltipEffect;
import com.obscuria.tooltips.client.style.frame.BonesFrame;
import com.obscuria.tooltips.client.style.frame.TextureFrame;
import com.obscuria.tooltips.client.style.frame.TooltipFrame;
import com.obscuria.tooltips.client.style.icon.*;
import com.obscuria.tooltips.client.style.panel.ColorRectPanel;
import com.obscuria.tooltips.client.style.panel.TooltipPanel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("all")
public final class TooltipsRegistry {
    private static final HashMap<ResourceLocation, TooltipElement<? extends TooltipPanel>> PANELS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipElement<? extends TooltipFrame>> FRAMES = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipElement<? extends TooltipIcon>> ICONS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipElement<? extends TooltipEffect>> EFFECTS = new HashMap<>();

    public static final TooltipElement<ColorRectPanel> PANEL_COLOR_RECT;
    public static final TooltipElement<TextureFrame> FRAME_TEXTURE;
    public static final TooltipElement<StaticIcon> ICON_STATIC;
    public static final TooltipElement<DescentSimpleIcon> ICON_DESCENT_SIMPLE;
    public static final TooltipElement<DescentComplexIcon> ICON_DESCENT_COMPLEX;
    public static final TooltipElement<DescentShineIcon> ICON_DESCENT_SHINE;
    public static final TooltipElement<ConstantRotationIcon> ICON_CONSTANT_ROTATION;
    public static final TooltipElement<EnchantmentEffect> EFFECT_ENCHANTMENT;
    public static final TooltipElement<EnderEffect> EFFECT_ENDER;

    static {
        PANEL_COLOR_RECT = registerPanel(factoryKey("color_rect"),
                params -> new ColorRectPanel(
                        FactoryHelper.color(params, "back_top"),
                        FactoryHelper.color(params, "back_bottom"),
                        FactoryHelper.color(params, "border_top"),
                        FactoryHelper.color(params, "border_bottom"),
                        FactoryHelper.color(params, "slot")));
        FRAME_TEXTURE = registerFrame(factoryKey("texture"),
                params -> new TextureFrame(new ResourceLocation(params.get("texture").getAsString())));
        ICON_STATIC = registerIcon(factoryKey("static"),
                params -> new StaticIcon());
        ICON_DESCENT_SIMPLE = registerIcon(factoryKey("descent_simple"),
                params -> new DescentSimpleIcon());
        ICON_DESCENT_COMPLEX = registerIcon(factoryKey("descent_complex"),
                params -> new DescentComplexIcon());
        ICON_DESCENT_SHINE = registerIcon(factoryKey("descent_shine"),
                params -> new DescentShineIcon(
                        FactoryHelper.color(params, "center_color"),
                        FactoryHelper.color(params, "start_color"),
                        FactoryHelper.color(params, "end_color"),
                        FactoryHelper.color(params, "particle_center_color"),
                        FactoryHelper.color(params, "particle_edge_color")));
        ICON_CONSTANT_ROTATION = registerIcon(factoryKey("constant_rotation"),
                params -> new ConstantRotationIcon());
        EFFECT_ENCHANTMENT = registerEffect(factoryKey("enchantment"),
                params -> new EnchantmentEffect(
                        FactoryHelper.color(params, "start_color"),
                        FactoryHelper.color(params, "end_color"),
                        FactoryHelper.color(params, "particle_center_color"),
                        FactoryHelper.color(params, "particle_edge_color")));
        EFFECT_ENDER = registerEffect(factoryKey("ender"),
                params -> new EnderEffect(
                        FactoryHelper.color(params, "center_color"),
                        FactoryHelper.color(params, "edge_color")));
    }

    public static final TooltipElement<ColorRectPanel> BUILTIN_PANEL_DEFAULT;
    public static final TooltipElement<ColorRectPanel> BUILTIN_PANEL_SILVER;
    public static final TooltipElement<ColorRectPanel> BUILTIN_PANEL_GOLDEN;
    public static final TooltipElement<TooltipFrame> BUILTIN_FRAME_BLANK;
    public static final TooltipElement<BonesFrame> BUILTIN_FRAME_BONES;
    public static final TooltipElement<TextureFrame> BUILTIN_FRAME_SILVER;
    public static final TooltipElement<TextureFrame> BUILTIN_FRAME_GOLDEN;
    public static final TooltipElement<DescentSimpleIcon> BUILTIN_ICON_COMMON;
    public static final TooltipElement<DescentComplexIcon> BUILTIN_ICON_RARE;
    public static final TooltipElement<DescentShineIcon> BUILTIN_ICON_EPIC;
    public static final TooltipElement<EnchantmentEffect> BUILTIN_EFFECT_ENCHANTMENT_GENERAL;
    public static final TooltipElement<EnchantmentEffect> BUILTIN_EFFECT_ENCHANTMENT_CURSE;
    public static final TooltipElement<EnderEffect> BUILTIN_EFFECT_ENDER;

    static {
        BUILTIN_PANEL_DEFAULT = registerPanel(builtinKey("default"),
                params -> new ColorRectPanel(0xf0100010, 0xf0100010, 0x505000FF, 0x5028007f, 0x20FFFFFF));
        BUILTIN_PANEL_SILVER = registerPanel(builtinKey("silver"),
                params -> new ColorRectPanel(0xf0100010, 0xf0100010, 0x80bbbbcc, 0x60606070, 0x20FFFFFF));
        BUILTIN_PANEL_GOLDEN = registerPanel(builtinKey("golden"),
                params -> new ColorRectPanel(0xf0100010, 0xf0100010, 0x80ffbb00, 0x60aa5000, 0x20FFFFFF));
        BUILTIN_FRAME_BLANK = registerFrame(builtinKey("blank"),
                params -> (renderer, pos, size) -> {});
        BUILTIN_FRAME_BONES = registerFrame(builtinKey("bones"),
                params -> new BonesFrame());
        BUILTIN_FRAME_SILVER = registerFrame(builtinKey("silver"),
                params -> new TextureFrame(new ResourceLocation(ObscureTooltips.MODID, "tooltips/builtin/decoration/silver.png")));
        BUILTIN_FRAME_GOLDEN = registerFrame(builtinKey("golden"),
                params -> new TextureFrame(new ResourceLocation(ObscureTooltips.MODID, "tooltips/builtin/decoration/golden.png")));
        BUILTIN_ICON_COMMON = registerIcon(builtinKey("common"),
                params -> new DescentSimpleIcon());
        BUILTIN_ICON_RARE = registerIcon(builtinKey("rare"),
                params -> new DescentComplexIcon());
        BUILTIN_ICON_EPIC = registerIcon(builtinKey("epic"),
                params -> new DescentShineIcon(0xffffffff, 0xfff00fff, 0x00ff00ff, 0xffffffff, 0xffff60ff));
        BUILTIN_EFFECT_ENCHANTMENT_GENERAL = registerEffect(builtinKey("enchantment"),
                params -> new EnchantmentEffect(0x30f00fff, 0x00ff00ff, 0x80ff80ff, 0x00aa40aa));
        BUILTIN_EFFECT_ENCHANTMENT_CURSE = registerEffect(builtinKey("enchantment_curse"),
                params -> new EnchantmentEffect(0x30ff0020, 0x00ff0020, 0x80ff2080, 0x00aa2040));
        BUILTIN_EFFECT_ENDER = registerEffect(builtinKey("ender"),
                params -> new EnderEffect(0x90ff60ff, 0x90ff00ff));
    }

    public static <T extends TooltipPanel> TooltipElement<T> registerPanel(ResourceLocation key, TooltipElement<T> factory) {
        PANELS.put(key, factory);
        return factory;
    }

    public static <T extends TooltipFrame> TooltipElement<T> registerFrame(ResourceLocation key, TooltipElement<T> factory) {
        FRAMES.put(key, factory);
        return factory;
    }

    public static <T extends TooltipIcon> TooltipElement<T> registerIcon(ResourceLocation key, TooltipElement<T> factory) {
        ICONS.put(key, factory);
        return factory;
    }

    public static <T extends TooltipEffect> TooltipElement<T> registerEffect(ResourceLocation key, TooltipElement<T> factory) {
        EFFECTS.put(key, factory);
        return factory;
    }

    public static Optional<TooltipPanel> getPanel(ResourceLocation key) {
        try {
            return PANELS.containsKey(key) ? Optional.of(PANELS.get(key).get()) : Optional.empty();
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to get Panel [{}]: expected constant, but got factory", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipFrame> getFrame(ResourceLocation key) {
        try {
            return FRAMES.containsKey(key) ? Optional.of(FRAMES.get(key).get()) : Optional.empty();
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to get Frame [{}]: expected constant, but got factory", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipIcon> getIcon(ResourceLocation key) {
        try {
            return ICONS.containsKey(key) ? Optional.of(ICONS.get(key).get()) : Optional.empty();
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to get Icon [{}]: expected constant, but got factory", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipEffect> getEffect(ResourceLocation key) {
        try {
            return EFFECTS.containsKey(key) ? Optional.of(EFFECTS.get(key).get()) : Optional.empty();
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to get Effect [{}]: expected constant, but got factory", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipPanel> buildPanel(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(PANELS.get(key).build(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to build custom Panel from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipFrame> buildFrame(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(FRAMES.get(key).build(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to build Frame from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipIcon> buildIcon(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(ICONS.get(key).build(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to build Icon from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<TooltipEffect> buildEffect(ResourceLocation key, @Nullable JsonObject params) {
        try {
            return Optional.of(EFFECTS.get(key).build(params));
        } catch (Exception e) {
            ObscureTooltips.LOGGER.error("Failed to build Effect from factory [{}]", key);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Contract("_ -> new")
    private static ResourceLocation factoryKey(String key) {
        return new ResourceLocation(ObscureTooltips.MODID, key);
    }

    @Contract("_ -> new")
    private static ResourceLocation builtinKey(String key) {
        return new ResourceLocation(ObscureTooltips.MODID, "builtin_"+key);
    }

    public static void setup() {}
}
