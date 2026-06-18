package dev.obscuria.obscure_tooltips.client.tooltip;

import dev.obscuria.obscure_tooltips.client.TooltipState;
import dev.obscuria.obscure_tooltips.config.ClientConfig;
import dev.obscuria.obscure_tooltips.util.easing.Easing;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;

public final class TooltipScroll {
    @Getter
    private static float scroll;

    private static boolean isActive;
    private static int maxScroll;
    private static float startScroll;
    private static float endScroll;

    private static long lastInputTime;
    private static long lastUpdateTime;

    public static boolean shouldCaptureInput() {
        if (!ClientConfig.SCROLL_ENABLED.get()) return false;
        if (!isActive || lastUpdateTime == 0) return false;
        return (Minecraft.getSystemTime() - lastUpdateTime) <= 200;
    }

    public static void onInput(float offset) {
        final var scrollSpeed = (float) ClientConfig.SCROLL_SPEED.get();
        final var targetScroll = clampScroll(endScroll + offset * scrollSpeed);
        if (endScroll == targetScroll) return;
        lastInputTime = Minecraft.getSystemTime();
        startScroll = scroll;
        endScroll = targetScroll;
    }

    public static void update(TooltipState state, int tooltipHeight, int screenHeight) {
        if (!ClientConfig.SCROLL_ENABLED.get()) return;
        final var overflow = Math.max(0, tooltipHeight - Math.max(0, screenHeight));
        isActive = overflow > 0;
        if (isActive) {
            maybeSetup(state, overflow);
            final var currentTime = Minecraft.getSystemTime();
            final var scrollProgress = (currentTime - lastInputTime) / 100f;
            final var scrollDelta = Easing.EASE_OUT_CUBIC.compute(Math.min(1f, scrollProgress));
            scroll = startScroll + scrollDelta * (endScroll - startScroll);
            lastUpdateTime = currentTime;
        }
        else {
            scroll = 0;
            maxScroll = 0;
            startScroll = 0;
            endScroll = 0;
            lastUpdateTime = 0;
        }
    }

    private static void maybeSetup(TooltipState state, int overflow) {
        if (!state.isInitialFrame() && maxScroll == overflow) return;
        lastUpdateTime = Minecraft.getSystemTime();
        maxScroll = overflow;
        scroll = maxScroll();
        startScroll = scroll;
        endScroll = scroll;
    }

    private static float minScroll() {
        return -ClientConfig.SCROLL_MARGIN.get();
    }

    private static float maxScroll() {
        return maxScroll + ClientConfig.SCROLL_MARGIN.get();
    }

    private static float clampScroll(float scroll) {
        return MathHelper.clamp(scroll, minScroll(), maxScroll());
    }
}
