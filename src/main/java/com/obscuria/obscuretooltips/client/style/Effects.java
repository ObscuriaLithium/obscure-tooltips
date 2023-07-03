package com.obscuria.obscuretooltips.client.style;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("unused")
public class Effects {
    public enum Order {
        LAYER_1_BACK,
        LAYER_2_BACK$TEXT,
        LAYER_3_TEXT$FRAME,
        LAYER_4_FRAME$ICON,
        LAYER_5_FRONT
    }

    public enum Category {
        NONE,
        ENCHANTMENT
    }
}
