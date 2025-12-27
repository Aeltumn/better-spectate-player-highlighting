package com.aeltumn.bsph.mixin;

import com.aeltumn.bsph.ColorChanger;
import com.aeltumn.bsph.GlowingState;
import com.aeltumn.bsph.config.GlowingConfig;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Overrides whether entities should appear as glowing and with which color.
 */
@Mixin(EntityRenderer.class)
public class GlowingStateOverrideMixin {

    @WrapOperation(method = "extractRenderState", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/entity/state/EntityRenderState;outlineColor:I"))
    private <T extends Entity> void modifyGlowingColor(
            EntityRenderState instance,
            int value,
            Operation<Void> original,
            @Local(argsOnly = true) T entity) {
        if (GlowingState.shouldShowGlowing(entity)) {
            var baseColor = ARGB.opaque(entity.getTeamColor());
            var config = GlowingConfig.get();
            if (config.showGlowing && entity.isCurrentlyGlowing()) {
                original.call(instance, ColorChanger.brighten(baseColor));
            } else if (config.showInvisibility && entity.isInvisible()) {
                original.call(instance, ColorChanger.darken(baseColor));
            } else {
                original.call(instance, baseColor);
            }
        } else {
            original.call(instance, value);
        }
    }
}
