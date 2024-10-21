package nl.enjarai.cicadatest.mixin;

//? if >1.19.4
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.math.MatrixStack;
import nl.enjarai.cicada.api.cursed.DummyClientPlayerEntity;
import nl.enjarai.cicada.api.screen.DrawUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    //? if >1.19.4 {
    @Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void renderTests(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        DrawUtils.drawEntityFollowingMouse(context.getMatrices(), 100, 100, 40, 0, mouseX, mouseY, DummyClientPlayerEntity.getInstance());
    }
    //?} else {
    /*@Inject(
            method = "render",
            at = @At("TAIL")
    )
    private void renderTests(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        DrawUtils.drawEntityFollowingMouse(matrices, 100, 100, 40, 0, mouseX, mouseY, DummyClientPlayerEntity.getInstance());
    }
    *///?}
}
