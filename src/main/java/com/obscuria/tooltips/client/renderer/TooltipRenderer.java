package com.obscuria.tooltips.client.renderer;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import com.obscuria.tooltips.ObscureTooltipsConfig;
import com.obscuria.tooltips.client.StyleManager;
import com.obscuria.tooltips.client.style.Effects;
import com.obscuria.tooltips.client.style.TooltipStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2ic;

import java.awt.*;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public final class TooltipRenderer {
    @Nullable private static TooltipStyle renderStyle = null;
    @Nullable private static ArmorStand renderStand;
    private static ItemStack renderStack = ItemStack.EMPTY;
    private static long tooltipStartMillis = 0;
    private static float tooltipSeconds = 0f;

    public static boolean render(TooltipContext renderer, ItemStack stack, Font font, List<ClientTooltipComponent> components, int x, int y, ClientTooltipPositioner positioner) {
        updateStyle(stack);
        if (renderStyle == null || components.isEmpty()) return false;

        renderer.define(renderStack, tooltipSeconds);
        final Component summaryField = getRarityName(stack);
        final Point size = calculateSize(font, components, summaryField);
        final Vector2ic rawPos = positioner.positionTooltip(renderer.width(), renderer.height(), x, y, size.x, size.y);
        final Vec2 pos = new Vec2(rawPos.x(), rawPos.y());

        renderer.pose().pushPose();
        renderer.drawManaged(() -> renderStyle.renderEffects(Effects.Order.LAYER_1_BACK, renderer, pos, size));
        renderer.drawManaged(() -> renderStyle.renderBack(renderer, pos, size, true));
        renderer.pose().translate(0F, 0F, 400F);
        renderer.context().drawString(Minecraft.getInstance().font, summaryField, (int) pos.x + 26, (int) pos.y + 13, 0xff505050);
        renderer.drawManaged(() -> renderStyle.renderEffects(Effects.Order.LAYER_2_BACK$TEXT, renderer, pos, size));
        renderText(renderer, font, components, pos);
        renderImages(renderer, font, components, pos);
        renderer.drawManaged(() -> renderStyle.renderFront(renderer, pos, size));
        renderer.drawManaged(() -> renderStyle.renderEffects(Effects.Order.LAYER_5_FRONT, renderer, pos, size));
        renderer.pose().popPose();

        if (stack.getItem() instanceof ArmorItem && ObscureTooltipsConfig.Client.displayArmorModels.get()) {
            final Vec2 center = renderSecondPanel(renderer, pos);
            equip(stack);
            renderStand(renderer, center.add(new Vec2(0, 26)));
        } else if (stack.getItem() instanceof TieredItem && ObscureTooltipsConfig.Client.displayToolModels.get()) {
            final Vec2 center = renderSecondPanel(renderer, pos);
            renderer.pose().pushPose();
            renderer.pose().translate(center.x, center.y, 500);
            renderer.pose().scale(2.75f, 2.75f, 2.75f);
            renderer.pose().mulPose(Axis.XP.rotationDegrees(-30));
            renderer.pose().mulPose(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * -20f));
            renderer.pose().mulPose(Axis.ZP.rotationDegrees(-45));
            renderer.pose().pushPose();
            renderer.pose().translate(-8, -8, -150);
            renderer.context().renderItem(stack, 0, 0);
            renderer.pose().popPose();
            renderer.pose().popPose();
        }

        renderer.flush();
        return true;
    }

    @SuppressWarnings("all")
    private static Vec2 renderSecondPanel(TooltipContext renderer, Vec2 pos) {
        renderer.drawManaged(() -> renderStyle.renderBack(renderer, pos.add(new Vec2(-55, 0)), new Point(30, 60), false));
        return pos.add(new Vec2(-40, 30));
    }

    @Contract("_ -> new")
    private static Component getRarityName(ItemStack stack) {
        return Component.translatable("rarity." + stack.getRarity().name().toLowerCase() + ".name");
    }

    private static Point calculateSize(Font font, List<ClientTooltipComponent> components, Component summaryField) {
        int width = 26 + components.get(0).getWidth(font);
        int height = 14;
        for (ClientTooltipComponent component : components) {
            int componentWidth = component.getWidth(font);
            if (componentWidth > width) width = componentWidth;
            height += component.getHeight();
        }
        final int SummaryWidth = 26 + font.width(summaryField.getString());
        if (SummaryWidth > width) width = SummaryWidth;
        return new Point(width, height);
    }

    private static void renderText(TooltipContext renderer, Font font, List<ClientTooltipComponent> components, Vec2 pos) {
        int offset = (int) pos.y + 3;
        for (int i = 0; i < components.size(); ++i) {
            ClientTooltipComponent component = components.get(i);
            component.renderText(font, (int) pos.x + (i == 0 ? 26 : 0), offset, renderer.pose().last().pose(), renderer.bufferSource());
            offset += component.getHeight() + (i == 0 ? 13 : 0);
        }
    }

    private static void renderImages(TooltipContext renderer, Font font, List<ClientTooltipComponent> components, Vec2 pos) {
        int offset = (int) pos.y + 4;
        for (int i = 0; i < components.size(); ++i) {
            ClientTooltipComponent component = components.get(i);
            component.renderImage(font, (int) pos.x, offset, renderer.context());
            offset += component.getHeight() + (i == 0 ? 13 : 0);
        }
    }

    @SuppressWarnings("deprecation")
    private static void renderStand(TooltipContext renderer, Vec2 pos) {
        if (renderStand == null || Minecraft.getInstance().player == null) return;
        renderer.push(() -> {
            renderer.translate(pos.x, pos.y, 500f);
            renderer.scale(-30, -30, 30);
            renderer.mul(Axis.XP.rotationDegrees(25));
            renderer.mul(Axis.YP.rotationDegrees((float) (System.currentTimeMillis() / 1000.0 % 360.0) * 20f));
            Lighting.setupForEntityInInventory();
            EntityRenderDispatcher entityrenderdispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
            entityrenderdispatcher.setRenderShadow(false);
            RenderSystem.runAsFancy(() -> entityrenderdispatcher.render(renderStand, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F,
                    renderer.pose(), renderer.bufferSource(), 15728880));
            renderer.flush();
            entityrenderdispatcher.setRenderShadow(true);
        });
        Lighting.setupFor3DItems();
    }

    private static void equip(ItemStack stack) {
        if (renderStand == null) return;
        if (stack.getItem() instanceof ArmorItem armorItem) {
            renderStand.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            renderStand.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            renderStand.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            renderStand.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            renderStand.setItemSlot(armorItem.getEquipmentSlot(), stack);
        }
    }

    private static void updateStyle(ItemStack stack) {
        if (renderStand == null && Minecraft.getInstance().level != null)
            renderStand = new ArmorStand(EntityType.ARMOR_STAND, Minecraft.getInstance().level);
        if (stack.isEmpty()) reset();
        else {
            tooltipSeconds = (System.currentTimeMillis() - tooltipStartMillis) / 1000f;
            if (stack == renderStack) return;
            reset();
            renderStack = stack;
            renderStyle = StyleManager.getStyleFor(stack).orElse(null);
            if (renderStyle != null) renderStyle.reset();
        }
        renderStack = stack;
    }

    private static void reset() {
        if (renderStyle != null) renderStyle.reset();
        renderStyle = null;
        tooltipStartMillis = System.currentTimeMillis();
        tooltipSeconds = 0f;
    }

    public static void clear() {
        renderStack = ItemStack.EMPTY;
        if (renderStyle != null) renderStyle.reset();
        renderStyle = null;
    }
}
