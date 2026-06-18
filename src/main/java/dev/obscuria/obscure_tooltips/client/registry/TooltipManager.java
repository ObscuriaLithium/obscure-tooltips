package dev.obscuria.obscure_tooltips.client.registry;

import com.github.bsideup.jabel.Desugar;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.obscuria.obscure_tooltips.ObscureTooltips;
import dev.obscuria.obscure_tooltips.Tags;
import dev.obscuria.obscure_tooltips.client.TooltipRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class TooltipManager implements IResourceManagerReloadListener {
    public static final TooltipManager SHARED = new TooltipManager();
    private static final JsonParser JSON = new JsonParser();

    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager manager) {
        TooltipRenderer.reset();

        final Map<ResourceKind, List<Entry>> buckets = new EnumMap<>(ResourceKind.class);
        for (ResourceKind kind : ResourceKind.values()) {
            kind.spec.onReloadStart();
            buckets.put(kind, new ArrayList<>());
        }

        final BiConsumer<String, TooltipSource.StreamOpener> sink = (path, opener) -> {
            final Classified classified = classify(path);
            if (classified == null) {
                return;
            }
            try (InputStream stream = opener.open()) {
                read(classified, stream, buckets);
            } catch (IOException exception) {
                ObscureTooltips.LOGGER.error("Failed to read tooltip resource {}: {}", path, exception.getMessage());
            }
        };

        final ModContainer self = Loader.instance().getIndexedModList().get(Tags.MOD_ID);
        for (TooltipSource source : collectSources(self)) {
            source.forEach(sink);
        }

        for (ResourceKind kind : ResourceKind.values()) {
            for (Entry entry : buckets.get(kind)) {
                kind.spec.load(entry.key, entry.json);
            }
            kind.spec.onReloadEnd();
        }
    }

    private List<TooltipSource> collectSources(ModContainer self) {
        final List<TooltipSource> sources = new ArrayList<>();
        if (self != null) {
            final String base = "assets/" + self.getModId() + "/tooltips";
            sources.add(modJarSource(self, base, base));
        }
        for (ResourcePackRepository.Entry entry : Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries()) {
            final TooltipSource source = sourceFor(entry.getResourcePack(), self);
            if (source != null) {
                sources.add(source);
            }
        }
        return sources;
    }

    private TooltipSource sourceFor(IResourcePack pack, ModContainer self) {
        if (pack instanceof VibrantTooltipsPack) {
            return self == null ? null : modJarSource(self, "packs/vibrant_tooltips/assets", "assets");
        }

        if (pack instanceof AbstractResourcePack) {
            final File base = ((AbstractResourcePack) pack).resourcePackFile;
            return base.isDirectory() ? folderSource(base) : zipSource(base);
        }
        return null;
    }


    private static TooltipSource modJarSource(ModContainer mod, String base, String classifyPrefix) {
        return consumer -> CraftingHelper.findFiles(mod, base, null,
                (root, file) -> {
                    final String relative = root.relativize(file).toString().replace('\\', '/');
                    consumer.accept(classifyPrefix + "/" + relative, () -> Files.newInputStream(file));
                    return true;
                }, false, false);
    }

    private static TooltipSource folderSource(File base) {
        return consumer -> {
            final File assets = new File(base, "assets");
            final File[] domains = assets.listFiles(File::isDirectory);
            if (domains == null) {
                return;
            }
            for (File domain : domains) {
                walkFolder(base, new File(domain, "tooltips"), consumer);
            }
        };
    }

    private static void walkFolder(File base, File dir, BiConsumer<String, TooltipSource.StreamOpener> consumer) {
        final File[] children = dir.listFiles();
        if (children == null) {
            return;
        }
        for (File child : children) {
            if (child.isDirectory()) {
                walkFolder(base, child, consumer);
                continue;
            }
            final String path = base.toPath().relativize(child.toPath()).toString().replace('\\', '/');
            consumer.accept(path, () -> new FileInputStream(child));
        }
    }

    private static TooltipSource zipSource(File base) {
        return consumer -> {
            try (ZipFile zip = new ZipFile(base)) {
                final Enumeration<? extends ZipEntry> entries = zip.entries();
                while (entries.hasMoreElements()) {
                    final ZipEntry zipEntry = entries.nextElement();
                    if (zipEntry.isDirectory()) {
                        continue;
                    }
                    consumer.accept(zipEntry.getName(), () -> zip.getInputStream(zipEntry));
                }
            } catch (IOException exception) {
                ObscureTooltips.LOGGER.error("Failed to read resource pack {}: {}", base, exception.getMessage());
            }
        };
    }

    private Classified classify(String path) {
        if (!path.startsWith("assets/") || !path.endsWith(".json")) {
            return null;
        }

        final String afterAssets = path.substring("assets/".length());
        final int slash = afterAssets.indexOf('/');

        if (slash < 0) {
            return null;
        }

        final String domain = afterAssets.substring(0, slash);
        final String afterDomain = afterAssets.substring(slash + 1);
        for (ResourceKind kind : ResourceKind.values()) {
            final String prefix = kind.spec.resourceDir() + "/";
            if (afterDomain.startsWith(prefix)) {
                final String name = afterDomain.substring(prefix.length(), afterDomain.length() - ".json".length());
                return new Classified(kind, new ResourceLocation(domain, name));
            }
        }
        return null;
    }

    private void read(Classified classified, InputStream stream, Map<ResourceKind, List<Entry>> buckets) {
        try {
            final JsonObject json = JSON.parse(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
            buckets.get(classified.kind).add(new Entry(classified.key, json));
        } catch (Exception exception) {
            ObscureTooltips.LOGGER.error("Failed to parse tooltip resource {}: {}", classified.key, exception.getMessage());
        }
    }

    @FunctionalInterface
    interface TooltipSource {
        void forEach(BiConsumer<String, StreamOpener> consumer);

        @FunctionalInterface
        interface StreamOpener {
            InputStream open() throws IOException;
        }
    }

    @Desugar
    private record Classified(ResourceKind kind, ResourceLocation key) { }

    @Desugar
    private record Entry(ResourceLocation key, JsonObject json) {}
}
