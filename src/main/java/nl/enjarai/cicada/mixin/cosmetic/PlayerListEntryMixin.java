package nl.enjarai.cicada.mixin.cosmetic;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
/*? if >=1.20.2 {*/
import net.minecraft.client.util.SkinTextures;
/*?}*/
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.util.CapeHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Shadow
    @Final
    private GameProfile profile;

    /*? if <=1.20.1 {*//*
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
    *//*?} else {*/
    @Inject(method = "texturesSupplier", at = @At("HEAD"))
    private static void loadTextures(GameProfile profile, CallbackInfoReturnable<Supplier<SkinTextures>> cir) {
        CapeHandler.onLoadTexture(profile);
    }

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getCapeTexture(CallbackInfoReturnable<SkinTextures> cir) {
        CapeHandler handler = CapeHandler.fromProfile(profile);
        if (handler.hasCape()) {
            SkinTextures oldTextures = cir.getReturnValue();
            Identifier capeTexture = handler.getCapeTexture();

            /*? if >=1.21 {*/
            Identifier defaultElytraTexture = Identifier.ofVanilla("textures/entity/elytra.png");
            /*?} else {*/
            /*Identifier defaultElytraTexture = new Identifier("textures/entity/elytra.png");
            *//*?}*/

            Identifier elytraTexture = handler.hasElytra() ? capeTexture : defaultElytraTexture;
            SkinTextures newTextures = new SkinTextures(
                    oldTextures.texture(), oldTextures.textureUrl(),
                    capeTexture, elytraTexture,
                    oldTextures.model(), oldTextures.secure());
            cir.setReturnValue(newTextures);
        }
    }
    /*?}*/
}
