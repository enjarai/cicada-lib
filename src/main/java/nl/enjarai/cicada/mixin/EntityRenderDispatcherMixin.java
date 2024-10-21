package nl.enjarai.cicada.mixin;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Shadow public Camera camera;

    @Inject(
            method = {
                    "getSquaredDistanceToCamera(Lnet/minecraft/entity/Entity;)D",
                    "getSquaredDistanceToCamera(DDD)D"
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
