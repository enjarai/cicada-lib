package nl.enjarai.cicada.mixin.post_20_2;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.util.CapeHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {
    @Shadow
    @Final
    private GameProfile profile;

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
            Identifier elytraTexture = handler.hasElytra() ? capeTexture : new Identifier("textures/entity/elytra.png");
            SkinTextures newTextures = new SkinTextures(
                    oldTextures.texture(), oldTextures.textureUrl(),
                    capeTexture, elytraTexture,
                    oldTextures.model(), oldTextures.secure());
            cir.setReturnValue(newTextures);
        }
    }
}
