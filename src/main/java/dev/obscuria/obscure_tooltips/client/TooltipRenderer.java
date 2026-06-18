package dev.obscuria.obscure_tooltips.client;

import dev.obscuria.obscure_tooltips.client.component.TooltipComponent;
import dev.obscuria.obscure_tooltips.client.render.GuiGraphics;
import dev.obscuria.obscure_tooltips.client.tooltip.TooltipScroll;
import dev.obscuria.obscure_tooltips.client.tooltip.layout.ArmorPreviewLayout;
import dev.obscuria.obscure_tooltips.client.tooltip.layout.DefaultLayout;
import dev.obscuria.obscure_tooltips.client.tooltip.layout.ToolPreviewLayout;
import dev.obscuria.obscure_tooltips.client.tooltip.layout.TooltipLayout;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;

import java.util.ArrayList;
import java.util.List;

public final class TooltipRenderer {
    private static ItemStack lastStack = ItemStack.EMPTY;
    private static ItemStack actualStack = ItemStack.EMPTY;
    private static TooltipLayout<?> layout = DefaultLayout.INSTANCE;
    private static TooltipState state = new EmptyState();

    public static void reset() {
        lastStack = ItemStack.EMPTY;
        actualStack = ItemStack.EMPTY;
        layout = DefaultLayout.INSTANCE;
        state = new EmptyState();
    }

    public static boolean render(GuiGraphics graphics, FontRenderer font, List<TooltipComponent> components,
                                 int mouseX, int mouseY, int screenWidth, int screenHeight, ItemStack stack) {
        if (!ClientConfig.ENABLED.get()) return false;
        if (components.isEmpty()) return false;
        if (!perform(stack)) return false;
        if (state instanceof EmptyState) return false;

        components = new ArrayList<>(components);
        components = layout.rawProcessPreWrap(state, components, font);
        components = TooltipHelper.wrapLines(graphics, components, font);
        components = layout.rawProcessPostWrap(state, components, font);

        final var margin = ClientConfig.CONTENT_MARGIN.get();
        final var width = margin * 2 + TooltipHelper.widthOf(components, font);
        final var height = margin * 2 + TooltipHelper.heightOf(components) - 2;
        final var posX = positionX(screenWidth, mouseX, width);
        final var posY = positionY(screenHeight, mouseY, height);

        TooltipScroll.update(state, 6 + height, screenHeight);

        graphics.pose().pushMatrix();
        graphics.pose().translate(0.0F, TooltipScroll.getScroll(), 400.0F);

        state.renderPanel(graphics, posX, posY, width, height);
        state.renderEffects(graphics, posX, posY, width, height);

        graphics.pose().pushMatrix();
        graphics.pose().translate(0.0f, 0.0f, 2.0f);
        state.renderFrame(graphics, posX, posY, width, height);
        graphics.pose().popMatrix();

        var componentX = margin + posX;
        var componentY = margin + posY;
        for (var component : components) {
            component.renderText(font, componentX, componentY, graphics);
            component.renderImage(font, componentX, componentY, graphics);
            componentY += component.getHeight();
        }

        graphics.pose().popMatrix();

        lastStack = actualStack;
        actualStack = ItemStack.EMPTY;
        state.update();
        return true;
    }

    private static boolean perform(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }

        actualStack = stack;
        if (ItemStack.areItemsEqual(lastStack, actualStack) && ItemStack.areItemStackTagsEqual(lastStack, actualStack)) return true;
        layout = shouldShowArmorPreview(actualStack) ? ArmorPreviewLayout.INSTANCE
                : shouldShowToolPreview(actualStack) ? ToolPreviewLayout.INSTANCE
                  : DefaultLayout.INSTANCE;
        state = layout.extractState(actualStack);
        return true;
    }

    private static boolean shouldShowArmorPreview(ItemStack stack) {
        if (!ClientConfig.ARMOR_PREVIEW_ENABLED.get()) return false;
        if (ClientConfig.isInArmorPreviewBlacklist(stack.getItem())) return false;
        return stack.getItem() instanceof ItemArmor || ClientConfig.isInArmorPreviewWhitelist(stack.getItem());
    }

    private static boolean shouldShowToolPreview(ItemStack stack) {
        if (!ClientConfig.TOOL_PREVIEW_ENABLED.get()) return false;
        if (ClientConfig.isInToolPreviewBlacklist(stack.getItem())) return false;

        final var item = stack.getItem();
        return item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemHoe || ClientConfig.isInToolPreviewWhitelist(item);
    }

    private static int positionX(int screenWidth, int mouseX, int width) {
        int x = mouseX + 12;
        if (x + width > screenWidth) {
            x = mouseX - 16 - width;
            if (x < 4) {
                x = 4;
            }
        }
        return x;
    }

    private static int positionY(int screenHeight, int mouseY, int height) {
        int y = mouseY - 12;
        if (y < 4) {
            y = 4;
        } else if (y + height + 4 > screenHeight) {
            y = screenHeight - height - 4;
        }
        return y;
    }

    private static final class EmptyState extends TooltipState {
        private EmptyState() {
            super(ItemStack.EMPTY);
        }
    }
}
