package com.aeltumn.bsph;

import com.aeltumn.bsph.config.GlowingConfig;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraft.world.scores.TeamColor;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adds additional keybinds for making players highlighted while spectating.
 */
public class BetterSpectatePlayerHighlightingModClient implements ClientModInitializer {
    private static final Map<TeamColor, Pair<String, Integer>> GLOW_TEAMS = new HashMap<>();

    static {
        GLOW_TEAMS.put(TeamColor.RED, Pair.of("red", GLFW.GLFW_KEY_KP_7));
        GLOW_TEAMS.put(TeamColor.GOLD, Pair.of("orange", GLFW.GLFW_KEY_KP_8));
        GLOW_TEAMS.put(TeamColor.YELLOW, Pair.of("yellow", GLFW.GLFW_KEY_KP_9));
        GLOW_TEAMS.put(TeamColor.GREEN, Pair.of("lime", GLFW.GLFW_KEY_KP_4));
        GLOW_TEAMS.put(TeamColor.DARK_GREEN, Pair.of("green", GLFW.GLFW_KEY_KP_5));
        GLOW_TEAMS.put(TeamColor.DARK_AQUA, Pair.of("cyan", GLFW.GLFW_KEY_KP_6));
        GLOW_TEAMS.put(TeamColor.AQUA, Pair.of("aqua", GLFW.GLFW_KEY_KP_1));
        GLOW_TEAMS.put(TeamColor.BLUE, Pair.of("blue", GLFW.GLFW_KEY_KP_2));
        GLOW_TEAMS.put(TeamColor.DARK_PURPLE, Pair.of("purple", GLFW.GLFW_KEY_KP_3));
        GLOW_TEAMS.put(TeamColor.LIGHT_PURPLE, Pair.of("pink", GLFW.GLFW_KEY_KP_0));
        GLOW_TEAMS.put(TeamColor.DARK_RED, Pair.of("crimson", null));
        GLOW_TEAMS.put(TeamColor.DARK_BLUE, Pair.of("navy", null));
        GLOW_TEAMS.put(TeamColor.WHITE, Pair.of("white", null));
        GLOW_TEAMS.put(TeamColor.GRAY, Pair.of("gray", null));
        GLOW_TEAMS.put(TeamColor.DARK_GRAY, Pair.of("dark", null));
        GLOW_TEAMS.put(TeamColor.BLACK, Pair.of("black", null));
    }

    /**
     * All keybinds mapped to the chat codes they modify.
     */
    private final Map<KeyMapping, List<TeamColor>> keybinds = new HashMap<>();

    /**
     * The category to use for the keybinds.
     */
    private KeyMapping.Category category;

    @Override
    public void onInitializeClient() {
        category = KeyMapping.Category.register(Identifier.fromNamespaceAndPath("bsph", "bsph"));

        for (var team : GLOW_TEAMS.entrySet()) {
            register("key.bsph.glow." + team.getValue().getFirst(), team.getValue().getSecond(), List.of(team.getKey()));
        }
        register("key.bsph.glow.all", GLFW.GLFW_KEY_KP_ADD, new ArrayList<>(GLOW_TEAMS.keySet()));
        register("key.bsph.glow.none", GLFW.GLFW_KEY_KP_SUBTRACT, List.of());

        // Set up an end of tick listener to go through each keybind and check for their usage
        ClientTickEvents.END_CLIENT_TICK.register((ignored) -> {
            var config = GlowingConfig.get();
            switch (config.activationMode) {
                case TOGGLE -> {
                    keybinds.forEach((key, colors) -> {
                        while (key.consumeClick()) {
                            if (colors.isEmpty()) {
                                GlowingState.INSTANCE.reset();
                            } else if (colors.size() > 1) {
                                GlowingState.INSTANCE.getGlowingTeams().addAll(colors);
                            } else {
                                var color = colors.getFirst();
                                if (GlowingState.INSTANCE.getGlowingTeams().contains(color)) {
                                    GlowingState.INSTANCE.getGlowingTeams().remove(color);
                                } else {
                                    GlowingState.INSTANCE.getGlowingTeams().add(color);
                                }
                            }
                        }
                    });
                }
                case HOLD -> {
                    var show = new ArrayList<TeamColor>();
                    keybinds.forEach((key, colors) -> {
                        while (key.consumeClick()) {
                            show.addAll(colors);
                        }
                    });
                    GlowingState.INSTANCE.setGlowingTeams(show);
                }
            }
        });
    }

    /**
     * Registers a new key binding with the given colors.
     */
    private void register(String translationKey, Integer code, List<TeamColor> colors) {
        var key = new KeyMapping(translationKey, InputConstants.Type.KEYSYM, code == null ? InputConstants.UNKNOWN.getValue() : code, category);
        KeyMappingHelper.registerKeyMapping(key);
        keybinds.put(key, colors);
    }
}
