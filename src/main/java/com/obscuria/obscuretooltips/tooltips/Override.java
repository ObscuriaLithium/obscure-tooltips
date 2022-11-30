package com.obscuria.obscuretooltips.tooltips;

import com.obscuria.obscuretooltips.Resources;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Override {
    public Style STYLE = Resources.DEFAULT_STYLE;
    public String RENDER = "model";
    public String TYPE = "none";
    public float SCALE = 2;
    public int X_OFFSET;
    public int Y_OFFSET;

    public boolean hasStyle = false;
    public boolean hasRender = false;
    public boolean hasType = false;
    public boolean hasScale = false;

    public Override(int xOffset, int yOffset) {
        this.X_OFFSET = xOffset;
        this.Y_OFFSET = yOffset;
    }

    public Override setStyle(Style style) {
        this.STYLE = style;
        this.hasStyle = true;
        return this;
    }

    public Override setRender(String render) {
        this.RENDER = render;
        this.hasRender = true;
        return this;
    }

    public Override setType(String type) {
        this.TYPE = type;
        this.hasType = true;
        return this;
    }

    public Override setScale(float scale) {
        this.SCALE = scale;
        this.hasScale = true;
        return this;
    }
}
