package nl.enjarai.cicada.mixin;

import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
//? if >=1.21.9 {
import net.minecraft.client.render.entity.EntityRenderManager;
//?} else {
/*import net.minecraft.client.render.entity.EntityRenderDispatcher;*/
//?}
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderManager.class)
public class EntityRenderDispatcherMixin {
    @Shadow public Camera camera;

    @Inject(
            method = {
                    "getSquaredDistanceToCamera(Lnet/minecraft/entity/Entity;)D"
            },
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelDistanceCheckIfOutsideWorld(CallbackInfoReturnable<Double> cir) {
        if (camera == null) {
            cir.setReturnValue(Double.MAX_VALUE);
        }
    }
}
