package nl.enjarai.cicada.mixin;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
/*? if >=1.21.2 {*/
import net.minecraft.client.render.entity.state.EntityRenderState;
import nl.enjarai.cicada.api.render.RenderStateUpdateEvent;
/*?}*/
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(
            method = "updateRenderState",
            at = @At("HEAD")
    )
    private void triggerEvent(Entity entity, EntityRenderState state, float tickDelta, CallbackInfo ci) {
        RenderStateUpdateEvent.allInvoker().onStateUpdate(entity, state, tickDelta);
    }
}
