package dev.obscuria.obscure_tooltips.client.registry;

import dev.obscuria.obscure_tooltips.ObscureTooltips;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public final class VibrantTooltipsPack extends AbstractResourcePack {
    private static final String ROOT = "packs/vibrant_tooltips";
    private static final String DISPLAY_NAME = "Vibrant Tooltips";

    private static final Set<String> DOMAINS = Collections.singleton("vibrant_tooltips");

    private static final VibrantTooltipsPack INSTANCE = new VibrantTooltipsPack();

    private final ClassLoader loader = VibrantTooltipsPack.class.getClassLoader();

    public VibrantTooltipsPack() {
        super(new File(ROOT));
    }

    public static void injectInto(ResourcePackRepository repository, List<ResourcePackRepository.Entry> available) {
        final IResourcePack pack = get();

        for (ResourcePackRepository.Entry existing : available) {
            if (existing.getResourcePackName().equals(pack.getPackName())) {
                return;
            }
        }

        try {
            final ResourcePackRepository.Entry entry = repository.new Entry(pack);
            entry.updateResourcePack();
            available.add(entry);
        } catch (IOException exception) {
            ObscureTooltips.LOGGER.error("Failed to read built-in resource pack metadata: {}", exception.getMessage());
        }
    }

    public static IResourcePack get() {
        return INSTANCE;
    }

    @Override
    @MethodsReturnNonnullByDefault
    protected InputStream getInputStreamByName(@Nonnull String name) throws IOException {
        final InputStream stream = loader.getResourceAsStream(ROOT + "/" + name);
        if (stream == null) {
            throw new IOException("Missing resource '" + name + "' in built-in pack '" + ROOT + "'");
        }
        return stream;
    }

    @Override
    protected boolean hasResourceName(@Nonnull String name) {
        return loader.getResource(ROOT + "/" + name) != null;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public Set<String> getResourceDomains() {
        return DOMAINS;
    }

    @Override
    @MethodsReturnNonnullByDefault
    public String getPackName() {
        return DISPLAY_NAME;
    }
}
