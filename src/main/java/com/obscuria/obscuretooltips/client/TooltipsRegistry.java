package com.obscuria.obscuretooltips.client;

import com.obscuria.obscuretooltips.ObscureTooltips;
import com.obscuria.obscuretooltips.client.style.effect.EnchantmentEffect;
import com.obscuria.obscuretooltips.client.style.effect.TailsEffect;
import com.obscuria.obscuretooltips.client.style.effect.TooltipEffect;
import com.obscuria.obscuretooltips.client.style.frame.BonesFrame;
import com.obscuria.obscuretooltips.client.style.frame.TextureFrame;
import com.obscuria.obscuretooltips.client.style.frame.TooltipFrame;
import com.obscuria.obscuretooltips.client.style.icon.AnimatedIcon;
import com.obscuria.obscuretooltips.client.style.icon.EpicAnimatedIcon;
import com.obscuria.obscuretooltips.client.style.icon.FlatIcon;
import com.obscuria.obscuretooltips.client.style.icon.TooltipIcon;
import com.obscuria.obscuretooltips.client.style.panel.ColorRectPanel;
import com.obscuria.obscuretooltips.client.style.panel.TooltipPanel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public final class TooltipsRegistry {
    private static final HashMap<ResourceLocation, TooltipPanel> PANELS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipFrame> FRAMES = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipIcon> ICONS = new HashMap<>();
    private static final HashMap<ResourceLocation, TooltipEffect> EFFECTS = new HashMap<>();

    public static final ColorRectPanel PANEL_DEFAULT = registerPanel(key("default"), new ColorRectPanel(0xf0100010, 0xf0100010, 0x505000FF, 0x5028007f, 0x20FFFFFF));
    public static final ColorRectPanel PANEL_SILVER = registerPanel(key("silver"), new ColorRectPanel(0xf0100010, 0xf0100010, 0x80bbbbcc, 0x60606070, 0x20FFFFFF));
    public static final ColorRectPanel PANEL_GOLDEN = registerPanel(key("golden"), new ColorRectPanel(0xf0100010, 0xf0100010, 0x80ffbb00, 0x60aa5000, 0x20FFFFFF));

    public static final FlatIcon ICON_DEFAULT = registerIcon(key("default"), new FlatIcon());
    public static final AnimatedIcon ICON_ANIMATED = registerIcon(key("animated"), new AnimatedIcon());
    public static final EpicAnimatedIcon ICON_ANIMATED_EPIC = registerIcon(key("animated_epic"), new EpicAnimatedIcon());

    public static final TooltipFrame FRAME_DEFAULT = registerFrame(key("default"), (context, pos, size, seconds) -> {});
    public static final TextureFrame FRAME_SILVER = registerFrame(key("silver"), new TextureFrame(new ResourceLocation(ObscureTooltips.MODID, "tooltips/builtin/decoration/silver.png")));
    public static final TextureFrame FRAME_GOLDEN = registerFrame(key("golden"), new TextureFrame(new ResourceLocation(ObscureTooltips.MODID, "tooltips/builtin/decoration/golden.png")));
    public static final BonesFrame FRAME_BONES = registerFrame(key("bones"), new BonesFrame());

    public static final EnchantmentEffect ENCHANTMENT_EFFECT = registerEffect(key("enchanted"), new EnchantmentEffect());
    public static final TailsEffect TAILS_EFFECT = registerEffect(key("tails"), new TailsEffect());

    public static <T extends TooltipPanel> T registerPanel(ResourceLocation key, T panel) {
        PANELS.put(key, panel);
        return panel;
    }

    public static <T extends TooltipFrame> T registerFrame(ResourceLocation key, T frame) {
        FRAMES.put(key, frame);
        return frame;
    }

    public static <T extends TooltipIcon> T registerIcon(ResourceLocation key, T icon) {
        ICONS.put(key, icon);
        return icon;
    }

    public static <T extends TooltipEffect> T registerEffect(ResourceLocation key, T effect) {
        EFFECTS.put(key, effect);
        return effect;
    }

    public static Optional<TooltipPanel> searchPanel(ResourceLocation key) {
        return PANELS.containsKey(key) ? Optional.of(PANELS.get(key)) : Optional.empty();
    }

    public static Optional<TooltipFrame> searchFrame(ResourceLocation key) {
        return FRAMES.containsKey(key) ? Optional.of(FRAMES.get(key)) : Optional.empty();
    }

    public static Optional<TooltipIcon> searchIcon(ResourceLocation key) {
        return ICONS.containsKey(key) ? Optional.of(ICONS.get(key)) : Optional.empty();
    }

    public static Optional<TooltipEffect> searchEffect(ResourceLocation key) {
        return EFFECTS.containsKey(key) ? Optional.of(EFFECTS.get(key)) : Optional.empty();
    }

    @Contract("_ -> new")
    private static ResourceLocation key(String key) {
        return new ResourceLocation(ObscureTooltips.MODID, key);
    }

    public static void setup() {}
}
