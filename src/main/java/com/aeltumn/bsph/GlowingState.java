package com.aeltumn.bsph;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.TeamColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores the teams currently being made to glow.
 */
public class GlowingState {
    public static GlowingState INSTANCE = new GlowingState();

    private List<TeamColor> glowingTeams = new ArrayList<>();

    /**
     * Sets the glowing teams to the given list.
     */
    public void setGlowingTeams(List<TeamColor> glowingTeams) {
        this.glowingTeams = glowingTeams;
    }

    /**
     * Returns all teams that are currently glowing.
     */
    public List<TeamColor> getGlowingTeams() {
        return glowingTeams;
    }

    /**
     * Resets the glowing state.
     */
    public void reset() {
        glowingTeams.clear();
    }

    /**
     * Returns whether the given entity should be shown to glow.
     */
    public static boolean shouldShowGlowing(Entity entity) {
        var player = Minecraft.getInstance().player;

        // Only allow using the glowing outlines when flying is allowed == they are spectating
        if (player == null || !player.getAbilities().mayfly || entity.getTeam() == null) return false;

        // Check that the team color is in the glowing teams list
        var color = entity.getTeam().getColor();
        return color.filter(teamColor -> GlowingState.INSTANCE.getGlowingTeams().contains(teamColor)).isPresent();
    }
}
