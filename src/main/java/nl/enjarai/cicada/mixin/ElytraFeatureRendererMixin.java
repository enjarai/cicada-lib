package nl.enjarai.cicada.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import nl.enjarai.cicada.util.CapeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin {
    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;canRenderCapeTexture()Z"
            )
    )
    private boolean cicada$enableElytraCape(boolean canRenderCapeTexture, @Local AbstractClientPlayerEntity abstractClientPlayerEntity) {
        var handler = CapeHandler.fromProfile(abstractClientPlayerEntity.getGameProfile());

        return canRenderCapeTexture && handler.hasElytra();
    }
}
