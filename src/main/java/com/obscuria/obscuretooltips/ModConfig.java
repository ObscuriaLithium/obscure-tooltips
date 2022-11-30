package com.obscuria.obscuretooltips;

import com.obscuria.obscureapi.ObscureAPI;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ModConfig {

    public static class Client {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec CLIENT_SPEC;
        public static final ForgeConfigSpec.BooleanValue armorPreview;
        public static final ForgeConfigSpec.BooleanValue model;
        public static final ForgeConfigSpec.DoubleValue scale;

        static {
            BUILDER.push("Settings");
            armorPreview = BUILDER.worldRestart().comment("If enabled, armor model will be displayed next to tooltip.").define("ArmorPreview", true);
            model = BUILDER.worldRestart().comment("If enabled, item icons will be 3D. This option is", "overridden by entries from the resource pack.").define("3DModels", true);
            scale = BUILDER.worldRestart().comment("Basic icon scale. This option is overridden by entries from", "the resource pack.").defineInRange("Scale", 2.0, 1, 20);
            BUILDER.pop();
            CLIENT_SPEC = BUILDER.build();
        }
    }

    public static void init() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "Obscuria");
        try { Files.createDirectory(modConfigPath);
        } catch (FileAlreadyExistsException ignored) {
        } catch (IOException e) { ObscureAPI.LOGGER.error("Failed to create Obscuria config directory", e); }
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, Client.CLIENT_SPEC,
                "Obscuria/obscure-tooltips-client.toml");
    }
}

