package com.obscuria.obscuretooltips.tooltips;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import com.obscuria.obscureapi.utils.TextHelper;
import com.obscuria.obscuretooltips.ModConfig;
import com.obscuria.obscuretooltips.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TooltipRenderer {
    private static LivingEntity ARMOR_STAND = null;
    private static int tickLerp = 0;
    private static int tick = 0;

    public static void onTick(TickEvent.ClientTickEvent event) {
        if (ARMOR_STAND == null) ARMOR_STAND = Minecraft.getInstance().level != null ? EntityType.ARMOR_STAND.create(Minecraft.getInstance().level) : null;
        if (ARMOR_STAND != null) ARMOR_STAND.tickCount++;
        if (event.phase == TickEvent.Phase.START) { tickLerp = tick; tick = tick + 3; }
    }

    public static void onTooltip(@NotNull RenderTooltipEvent event) {
        if (event.getItemStack() == ItemStack.EMPTY) return;
        event.setCanceled(true);
        drawTooltip(event.getPoseStack(), event.getComponents(), event.getFont(), event.getItemStack(), event.getX(), event.getY());
    }

    private static void drawTooltip(PoseStack pose, @NotNull List<ClientTooltipComponent> components, Font font, ItemStack stack, int x, int y) {
        if (components.isEmpty() || Minecraft.getInstance().screen == null) return;
        final List<ClientTooltipComponent> lines = new ArrayList<>(components);
        final Override override = Resources.INSTANCE.getOverride(stack.getItem());
        final Style style = Resources.INSTANCE.getStyle(stack, override);
        final Component type = TextHelper.component("§7" + Resources.INSTANCE.getType(stack, override));
        final String render = Resources.INSTANCE.getRender(stack, override);
        final float scale = Resources.INSTANCE.getScale(stack, override);
        final int xOffset = override.X_OFFSET;
        final int yOffset = override.Y_OFFSET;

        int width = 0; int height = lines.size() == 1 ? -2 : 0;
        int totalLines = 0;
        if (lines.size() > 1 && lines.get(1).getWidth(font) == 0) lines.remove(1);
        for (ClientTooltipComponent clienttooltipcomponent : lines) {
            int lineWidth = 12 + clienttooltipcomponent.getWidth(font) + (totalLines == 0 ? 36 : 0);
            if (lineWidth > width) width = lineWidth;
            height += clienttooltipcomponent.getHeight() + (totalLines == 1 ? 27 : 0);
            totalLines++; }
        if (font.width(type) + 48 > width) width = font.width(type) + 48;
        height = Math.max(30, height) + 12;

        int xPos = x + 6; int yPos = y - 18;
        if (xPos + width > Minecraft.getInstance().screen.width) xPos -= 28 + width;
        if (yPos + height + 6 > Minecraft.getInstance().screen.height) yPos = Minecraft.getInstance().screen.height - height - 6;

        if (stack.getItem() instanceof ArmorItem && ModConfig.Client.armorPreview.get()) drawArmorPreview(pose, stack, xPos, yPos);

        drawFrame(style, pose, xPos, yPos, 400, width, height, true, true);

        pose.pushPose();
        float blitOffset = Minecraft.getInstance().getItemRenderer().blitOffset;
        Minecraft.getInstance().getItemRenderer().blitOffset = 400.0F;
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix4f = pose.last().pose();
        RenderSystem.enableDepthTest();
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
        MultiBufferSource.BufferSource multibuffersource$buffersource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        pose.translate(0.0D, 0.0D, 400.0D);
        int yPosLine = yPos + 11;
        for (int i = 0; i < lines.size(); ++i) {
            ClientTooltipComponent tooltipComponent = lines.get(i);
            tooltipComponent.renderText(font, xPos + 6 + (i == 0 ? 36 : 0), yPosLine, matrix4f, multibuffersource$buffersource);
            yPosLine += tooltipComponent.getHeight() + (i == 0 ? 2 : 0);
            if (i == 0) yPosLine += 20; }
        multibuffersource$buffersource.endBatch();
        pose.popPose();

        yPosLine = yPos + 11;
        for (int i = 0; i < lines.size(); ++i) {
            ClientTooltipComponent tooltipComponent = lines.get(i);
            tooltipComponent.renderImage(font, xPos + 6 + (i == 0 ? 36 : 0), yPosLine, pose, Minecraft.getInstance().getItemRenderer(), 400);
            yPosLine += tooltipComponent.getHeight() + (i == 0 ? 2 : 0);
            if (i == 0) yPosLine += 20; }

        pose.pushPose();
        pose.translate(xPos, yPos, 400);
        font.drawShadow(pose, type, 42, 22, 0);
        pose.popPose();

        Minecraft.getInstance().getItemRenderer().blitOffset = blitOffset;

        if (render.equals("model")) {
            renderItemModel(stack, xPos, yPos, scale, xOffset, yOffset);
        } else {
            renderItemFlat(stack, xPos, yPos, scale, xOffset, yOffset);
        }
    }

    private static void drawFrame(@NotNull Style style, @NotNull PoseStack pose, int xPos, int yPos, int depth, int width, int height, boolean item, boolean delimiter) {
        pose.pushPose(); pose.translate(xPos, yPos, depth);
        width++; height++;
        start();
        RenderSystem.setShaderTexture(0, style.BACKGROUND);
        GuiComponent.blit(pose, 0, 0, 0, 0, 4, 4, 8, 8);
        GuiComponent.blit(pose, 4 + Math.max(width - 8, 0), 0, 4, 0, 4, 4, 8, 8);
        GuiComponent.blit(pose, 0, 4 + Math.max(height - 8, 0), 0, 4, 4, 4, 8, 8);
        GuiComponent.blit(pose, 4 + Math.max(width - 8, 0), 4 + Math.max(height - 8, 0), 4, 4, 4, 4, 8, 8);
        if (width > 8) {
            pose.pushPose();
            pose.translate(4, 0, 0);
            pose.scale(width - 8, 1, 1);
            GuiComponent.blit(pose, 0, 0, 4, 0, 1, 4, 8, 8);
            pose.popPose();
            pose.pushPose();
            pose.translate(4, 4 + Math.max(height - 8, 0), 0);
            pose.scale(width - 8, 1, 1);
            GuiComponent.blit(pose, 0, 0, 4, 4, 1, 4, 8, 8);
            pose.popPose();
        }
        if (height > 8) {
            pose.pushPose();
            pose.translate(0, 4, 0);
            pose.scale(1, height - 8, 1);
            GuiComponent.blit(pose, 0, 0, 0, 4, 4, 1, 8, 8);
            pose.popPose();
            pose.pushPose();
            pose.translate(4 + Math.max(width - 8, 0), 4, 0);
            pose.scale(1, height - 8, 1);
            GuiComponent.blit(pose, 0, 0, 4, 4, 4, 1, 8, 8);
            pose.popPose();
        }
        if (width > 8 && height > 8) {
            pose.pushPose();
            pose.translate(4, 4, 0);
            pose.scale(width - 8, height - 8, 1);
            GuiComponent.blit(pose, 0, 0, 4, 4, 1, 1, 8, 8);
            pose.popPose();
        }
        end();
        width--; height--;
        start();
        RenderSystem.setShaderTexture(0, style.BORDER);
        pose.pushPose();
        pose.translate(1, 1, 0);
        pose.scale((width - 2) / 100F, 1, 1);
        GuiComponent.blit(pose, 0, 0, 0, 0, 100, 1, 100, 100);
        pose.popPose();
        pose.pushPose();
        pose.translate(1, height - 2, 0);
        pose.scale((width - 2) / 100F, 1, 1);
        GuiComponent.blit(pose, 0, 0, 0, 99, 100, 1, 100, 100);
        pose.popPose();
        pose.pushPose();
        pose.translate(1, 1, 0);
        pose.scale(1, (height - 2) / 100F, 1);
        GuiComponent.blit(pose, 0, 0, 0, 0, 1, 100, 100, 100);
        pose.popPose();
        pose.pushPose();
        pose.translate(width - 2, 1, 0);
        pose.scale(1, (height - 2) / 100F, 1);
        GuiComponent.blit(pose, 0, 0, 99, 0, 1, 100, 100, 100);
        pose.popPose();
        if (item) {
            pose.pushPose();
            pose.translate(7, 7, 0);
            pose.scale(2, 2, 1);
            GuiComponent.blit(pose, 0, 0, 2, 4, 24, 24, 100, 100);
            pose.popPose();
        }
        if (delimiter && height > 42) {
            pose.pushPose();
            pose.translate(3, 38, 0);
            pose.scale((width - 6) / 96F, 1, 1);
            GuiComponent.blit(pose, 0, 0, 2, 2, 96, 1, 100, 100);
            pose.popPose();
        }
        end();
        start();
        RenderSystem.setShaderTexture(0, style.DECOR);
        pose.pushPose();
        pose.translate(-1, -1, 0);
        GuiComponent.blit(pose, 0, 0, 0, 0, 16, 16, 80, 32);
        pose.popPose();
        pose.pushPose();
        pose.translate(-1, height - 15, 0);
        GuiComponent.blit(pose, 0, 0, 0, 16, 16, 16, 80, 32);
        pose.popPose();
        pose.pushPose();
        pose.translate(width - 15, -1, 0);
        GuiComponent.blit(pose, 0, 0, 64, 0, 16, 16, 80, 32);
        pose.popPose();
        pose.pushPose();
        pose.translate(width - 15, height - 15, 0);
        GuiComponent.blit(pose, 0, 0, 64, 16, 16, 16, 80, 32);
        pose.popPose();
        pose.pushPose();
        pose.translate(Math.round(width / 2F) - 24, -6, 0);
        GuiComponent.blit(pose, 0, 0, 16, 0, 48, 16, 80, 32);
        pose.popPose();
        pose.pushPose();
        pose.translate(Math.round(width / 2F) - 24, height - 10, 0);
        GuiComponent.blit(pose, 0, 0, 16, 16, 48, 16, 80, 32);
        pose.popPose();
        end();
        pose.popPose();
    }

    private static void renderItemModel(ItemStack stack, int xPos, int yPos, float scale, int xOffset, int yOffset) {
        final PoseStack pose = RenderSystem.getModelViewStack();
        final float rot = Mth.lerp(Minecraft.getInstance().getFrameTime(), tickLerp, tick);
        final float xO = (float) (Math.sin(Math.toRadians(rot)) * (150 * scale));
        final float zO = (float) (Math.cos(Math.toRadians(rot)) * (150 * scale));
        final float x02 = (float) (Math.sin(Math.toRadians(rot + 90)) * (8 * scale));
        final float zO2 = (float) (Math.cos(Math.toRadians(rot + 90)) * (8 * scale));
        pose.pushPose();
        pose.translate(xPos + 21 + xOffset - xO - x02, yPos + 13 + yOffset - 8 * (scale - 1), 400 - zO - zO2);
        pose.scale(scale, scale, scale);
        pose.mulPose(Vector3f.YP.rotationDegrees(rot));
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
        pose.popPose();
    }

    private static void renderItemFlat(ItemStack stack, int xPos, int yPos, float scale, int xOffset, int yOffset) {
        final PoseStack pose = RenderSystem.getModelViewStack();
        pose.pushPose();
        pose.translate(xPos + 13 + xOffset - 8 * (scale - 1), yPos + 13 + yOffset - 8 * (scale - 1), 400);
        pose.scale(scale, scale, scale);
        Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, 0, 0);
        pose.popPose();
    }

    private static void drawArmorPreview(PoseStack pose, ItemStack stack, int x, int y) {
        if (ARMOR_STAND == null) return;
        clearArmorStand();
        drawFrame(Resources.DEFAULT_STYLE, pose, x - 60, y, 400, 46, 80, false, false);
        ARMOR_STAND.setItemSlot(LivingEntity.getEquipmentSlotForItem(stack), stack.copy());
        renderEntity(x - 37, y + 68, 30, 0, 0, ARMOR_STAND);
    }

    private static void clearArmorStand() {
        if (ARMOR_STAND == null) return;
        for (EquipmentSlot slot : EquipmentSlot.values()) ARMOR_STAND.setItemSlot(slot, ItemStack.EMPTY);
        for (InteractionHand hand : InteractionHand.values()) ARMOR_STAND.setItemInHand(hand, ItemStack.EMPTY);
    }

    private static void renderEntity(int x, int y, int scale, float xHead, float yHead, @NotNull LivingEntity entity) {
        RenderSystem.enableDepthTest();
        float f = (float)Math.atan(xHead / 40.0F);
        float f1 = (float)Math.atan(yHead / 40.0F);
        PoseStack posestack = RenderSystem.getModelViewStack();
        posestack.pushPose();
        posestack.translate(x, y, 500.0D);
        posestack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack posestack1 = new PoseStack();
        posestack1.translate(0.0D, 0.0D, 0.0D);
        posestack1.scale((float)scale, (float)scale, (float)scale);
        Quaternion quaternion = Vector3f.XP.rotationDegrees(f1 * 20.0F);
        posestack1.mulPose(Vector3f.XP.rotationDegrees(200));
        posestack1.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(Minecraft.getInstance().getFrameTime(), tickLerp, tick)));
        float f2 = entity.yBodyRot;
        float f3 = entity.getYRot();
        float f4 = entity.getXRot();
        float f5 = entity.yHeadRotO;
        float f6 = entity.yHeadRot;
        Lighting.setupForEntityInInventory();
        EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion.conj();
        entityrenderdispatcher.overrideCameraOrientation(quaternion);
        entityrenderdispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, posestack1, multibuffersource$buffersource, 15728880));
        multibuffersource$buffersource.endBatch();
        entityrenderdispatcher.setRenderShadow(true);
        entity.yBodyRot = f2;
        entity.setYRot(f3);
        entity.setXRot(f4);
        entity.yHeadRotO = f5;
        entity.yHeadRot = f6;
        posestack.popPose();
        RenderSystem.applyModelViewMatrix();
        Lighting.setupFor3DItems();
    }

    private static void start() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    private static void end() {
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}