package nl.enjarai.cicada.api.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.LivingEntity;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class DrawUtils {
    public static void drawEntityFollowingMouse(
            DrawContext context, int x1, int y1, int x2, int y2, int size, float scale,
            double rotation, double mouseX, double mouseY, LivingEntity entity
    ) {
        float x = (x1 + x2) / 2.0F;
        float y = (y1 + y2) / 2.0F;

        float yaw = (float) (Math.atan((-mouseX + x) / 40.0F) * Math.sin((rotation / 180.0 + 0.5) * Math.PI));
        float pitch = (float) Math.atan((-mouseY + y - size * 0.65f) / 40.0F);

        Quaternionf entityRotation = new Quaternionf().rotateZ((float) Math.PI).rotateY((float) Math.toRadians(rotation));
        Quaternionf pitchRotation = new Quaternionf().rotateX(pitch * 20.0F * 0.017453292F);
        entityRotation.mul(pitchRotation);

        float oldBodyYaw = entity.bodyYaw;
        float oldYaw = entity.getYaw();
        float oldPitch = entity.getPitch();
        float oldPrevHeadYaw = entity.lastHeadYaw;

        float oldHeadYaw = entity.headYaw;
        entity.bodyYaw = 180.0F + yaw * 20.0F;
        entity.setYaw(180.0F + yaw * 40.0F);
        entity.setPitch(-pitch * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.lastHeadYaw = entity.getYaw();

        float o = entity.getScale();
        Vector3f vector3f = new Vector3f(0.0F, entity.getHeight() / 2.0F + scale * o, 0.0F);
        float p = (float) size / o;

        var entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> entityRenderer = entityRenderDispatcher.getRenderer(entity);
        var entityRenderState = entityRenderer.getAndUpdateRenderState(entity, 1.0F);
        entityRenderState.hitbox = null;
        context.addEntity(entityRenderState, p, vector3f, entityRotation, pitchRotation, x1, y1, x2, y2);

        entity.bodyYaw = oldBodyYaw;
        entity.setYaw(oldYaw);
        entity.setPitch(oldPitch);
        entity.lastHeadYaw = oldPrevHeadYaw;
        entity.headYaw = oldHeadYaw;
    }
}
