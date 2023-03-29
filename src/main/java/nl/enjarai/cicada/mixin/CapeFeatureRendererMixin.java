package nl.enjarai.cicada.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

// Fixes https://bugs.mojang.com/browse/MC-127749
@Mixin(CapeFeatureRenderer.class)
public abstract class CapeFeatureRendererMixin {
    @ModifyVariable(
            method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/client/network/AbstractClientPlayerEntity;FFFFFF)V",
            at = @At("STORE"),
            ordinal = 6
    )
    private float cicada$fixCapeInterpolation(float bodyRotation,
                                       @Local(argsOnly = true) AbstractClientPlayerEntity playerEntity,
                                       @Local(ordinal = 2, argsOnly = true) float tickDelta) {
        return playerEntity.prevBodyYaw + (playerEntity.bodyYaw - playerEntity.prevBodyYaw) * tickDelta;
    }
}
