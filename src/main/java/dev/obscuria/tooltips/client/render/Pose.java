package dev.obscuria.tooltips.client.render;

import net.minecraft.client.renderer.GlStateManager;

public final class Pose {
    public void pushMatrix() {
        GlStateManager.pushMatrix();
    }

    public void popMatrix() {
        GlStateManager.popMatrix();
    }

    public void translate(float x, float y, float z) {
        GlStateManager.translate(x, y, z);
    }

    public void scale(float x, float y, float z) {
        GlStateManager.scale(x, y, z);
    }

    public void rotateRadiansZ(float radians) {
        GlStateManager.rotate((float) Math.toDegrees(radians), 0.0F, 0.0F, 1.0F);
    }

    public void rotateZ(float degrees) {
        GlStateManager.rotate(degrees, 0.0F, 0.0F, 1.0F);
    }

    public void rotateY(float degrees) {
        GlStateManager.rotate(degrees, 0.0F, 1.0F, 0.0F);
    }
}
