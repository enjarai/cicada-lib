package nl.enjarai.cicada.mixin.imgui;

import net.minecraft.client.util.Window;
import nl.enjarai.cicada.api.imgui.ImMyGui;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class WindowMixin {
    @Shadow @Final private long handle;

    @Inject(
            method = "<init>",
            at = @At("TAIL")
    )
    private void initImgui(CallbackInfo ci) {
        ImMyGui.init(handle);
    }
}
