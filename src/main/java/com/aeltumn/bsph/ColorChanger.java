package com.aeltumn.bsph;

import net.minecraft.util.ARGB;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

/**
 * Makes the color of an outline brighter if they have glowing.
 */
public class ColorChanger {
    private static final Map<Integer, Integer> brighter = new HashMap<>();
    private static final Map<Integer, Integer> darker = new HashMap<>();

    /**
     * Brightens the given color.
     */
    public static int brighten(int input) {
        return brighter.computeIfAbsent(input, (base) -> modify(base, 1.33f));
    }

    /**
     * Darkens the given color.
     */
    public static int darken(int input) {
        return darker.computeIfAbsent(input, (base) -> modify(base, 0.67f));
    }

    /**
     * Modifies the brightness of input by factor.
     */
    private static int modify(int input, float factor) {
        var start = new Color(ARGB.red(input), ARGB.green(input), ARGB.blue(input));
        Color modified;
        if (factor > 1f) {
            modified = new Color(
                    Math.clamp((int) (start.getRed() + (255 - start.getRed()) * (factor - 1f)), 0, 255),
                    Math.clamp((int) (start.getGreen() + (255 - start.getGreen()) * (factor - 1f)), 0, 255),
                    Math.clamp((int) (start.getBlue() + (255 - start.getBlue()) * (factor - 1f)), 0, 255)
            );
        } else {
            var hsb = Color.RGBtoHSB(start.getRed(), start.getGreen(), start.getBlue(), null);
            modified = Color.getHSBColor(hsb[0], hsb[1], Math.clamp(hsb[2] * factor, 0f, 1f));
        }
        return ARGB.color(modified.getRed(), modified.getGreen(), modified.getBlue());
    }
}
