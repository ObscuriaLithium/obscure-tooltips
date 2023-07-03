package com.obscuria.obscuretooltips;

import com.obscuria.obscuretooltips.client.ResourceLoader;
import com.obscuria.obscuretooltips.client.TooltipsAssociations;
import com.obscuria.obscuretooltips.client.TooltipsFactory;
import com.obscuria.obscuretooltips.client.TooltipsRegistry;
import com.obscuria.obscuretooltips.client.style.TooltipStylePreset;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.item.Items;

public class ObscureTooltipsClient {
    public static void setup() {
        TooltipsRegistry.setup();
        TooltipsFactory.setup();
        if (Minecraft.getInstance().getResourceManager() instanceof ReloadableResourceManager manager)
            manager.registerReloadListener(ResourceLoader.INSTANCE);

        final TooltipStylePreset bones = new TooltipStylePreset.Builder().withFrame(TooltipsRegistry.FRAME_BONES).build();
        TooltipsAssociations.associate(Items.NETHERITE_SWORD, bones);
        TooltipsAssociations.associate(Items.ROTTEN_FLESH, bones);
        TooltipsAssociations.associate(Items.BONE, bones);
    }
}
