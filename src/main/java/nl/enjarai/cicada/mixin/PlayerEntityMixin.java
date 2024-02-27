package nl.enjarai.cicada.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import nl.enjarai.cicada.util.CapeHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Shadow public abstract GameProfile getGameProfile();

    @Inject(
            method = "isPartVisible",
            at = @At("HEAD"),
            cancellable = true
    )
    private void maybeHideHat(PlayerModelPart modelPart, CallbackInfoReturnable<Boolean> cir) {
        if (modelPart == PlayerModelPart.HAT) {
            var capeHandler = CapeHandler.fromProfile(getGameProfile());
            if (capeHandler.disableHeadOverlay()) {
                cir.setReturnValue(false);
            }
        }
    }
}
