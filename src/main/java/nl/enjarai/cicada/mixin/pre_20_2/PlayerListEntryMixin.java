package nl.enjarai.cicada.mixin.pre_20_2;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.util.CapeHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Shadow @Final private GameProfile profile;
    @Shadow private boolean texturesLoaded;

    @Inject(method = "loadTextures", at = @At("HEAD"))
    private void cicada$loadTextures(CallbackInfo ci) {
        if (!texturesLoaded) {
            CapeHandler.onLoadTexture(profile);
        }
    }

    @Inject(method = "getCapeTexture", at = @At("TAIL"), cancellable = true)
    private void cicada$getCapeTexture(CallbackInfoReturnable<Identifier> cir) {
        CapeHandler handler = CapeHandler.fromProfile(profile);
        if (handler.hasCape() || handler.hasElytra()) {
            cir.setReturnValue(handler.getCapeTexture());
        }
    }
}
