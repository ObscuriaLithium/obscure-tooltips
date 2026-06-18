package dev.obscuria.obscure_tooltips.config;

import dev.obscuria.obscure_tooltips.Tags;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.ArrayList;
import java.util.List;

public final class ConfigGuiScreen extends GuiConfig {

    public ConfigGuiScreen(GuiScreen parent) {
        super(parent, elements(), Tags.MOD_ID, false, false, Tags.MOD_NAME);
    }

    private static List<IConfigElement> elements() {
        final Configuration config = ClientConfig.configuration;
        final List<IConfigElement> list = new ArrayList<>(
                new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        for (String name : config.getCategoryNames()) {
            final ConfigCategory category = config.getCategory(name);
            if (!category.isChild() && !category.getName().equals(Configuration.CATEGORY_GENERAL)) {
                list.add(new ConfigElement(category));
            }
        }
        return list;
    }
}
