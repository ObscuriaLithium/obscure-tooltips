package com.obscuria.obscuretooltips.client.style.particle;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class TooltipParticle {
    protected final long START_TIME;
    protected final float LIFETIME;
    protected Vec2 position = Vec2.ZERO;

    public TooltipParticle(float lifetime) {
        START_TIME = System.currentTimeMillis();
        LIFETIME = lifetime;
    }

    public abstract void renderParticle(GuiGraphics context, float seconds);

    public final void render(GuiGraphics context) {
        renderParticle(context, (System.currentTimeMillis() - START_TIME) / 1000f);
    }

    public final boolean shouldRemove() {
        return (System.currentTimeMillis() - START_TIME) / 1000f > LIFETIME;
    }
}
