package com.obscuria.obscuretooltips.tooltips;

import com.obscuria.obscuretooltips.ObscureTooltipsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Style {
    public final ResourceLocation BACKGROUND;
    public final ResourceLocation BORDER;
    public final ResourceLocation DECOR;

    public Style(String background, String border, String decor) {
        this.BACKGROUND = new ResourceLocation(ObscureTooltipsMod.MODID, background);
        this.BORDER = new ResourceLocation(ObscureTooltipsMod.MODID, border);
        this.DECOR = new ResourceLocation(ObscureTooltipsMod.MODID, decor);
    }
}
