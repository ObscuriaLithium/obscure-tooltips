package dev.obscuria.tooltips.client.tooltip.layout;

import dev.obscuria.tooltips.client.TooltipState;
import dev.obscuria.tooltips.client.component.ArmorPreviewComponent;
import dev.obscuria.tooltips.client.component.SplitComponent;
import dev.obscuria.tooltips.client.component.TooltipComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public final class ArmorPreviewLayout extends AbstractHeaderLayout<ArmorPreviewLayout.State> {

    public static final ArmorPreviewLayout INSTANCE = new ArmorPreviewLayout();

    @Override
    public State extractState(ItemStack stack) {
        return new State(stack);
    }

    @Override
    public List<TooltipComponent> processPostWrap(State state, List<TooltipComponent> components, FontRenderer font) {
        if (state.armorStand == null) {
            return components;
        }
        return Collections.singletonList(new SplitComponent(new ArmorPreviewComponent(state.armorStand), components));
    }

    public static final class State extends TooltipState {

        @Nullable
        public final EntityArmorStand armorStand;

        State(ItemStack stack) {
            super(stack);
            this.armorStand = makeArmorStand(stack);
        }

        @Nullable
        private static EntityArmorStand makeArmorStand(ItemStack stack) {
            final World world = Minecraft.getMinecraft().world;
            if (world == null) {
                return null;
            }
            final var armorStand = new EntityArmorStand(world);
            final var slot = EntityLiving.getSlotForItemStack(stack);
            armorStand.setItemStackToSlot(slot, stack);
            return armorStand;
        }
    }
}
