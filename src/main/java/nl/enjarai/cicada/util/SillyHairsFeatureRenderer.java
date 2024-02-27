package nl.enjarai.cicada.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(EnvType.CLIENT)
public class SillyHairsFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    public SillyHairsFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> ctx) {
        super(ctx);
    }

    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity abstractClientPlayerEntity, float limbPos, float limbSpeed, float delta, float animationProgress, float headYaw, float headPitch) {
        var capeHandler = CapeHandler.fromProfile(abstractClientPlayerEntity.getGameProfile());
        if (capeHandler.hasSillyHairs()) {
            VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(capeHandler.getDecorationsTexture()));
            int overlay = LivingEntityRenderer.getOverlay(abstractClientPlayerEntity, 0.0F);

            float o = MathHelper.lerp(delta, abstractClientPlayerEntity.prevYaw, abstractClientPlayerEntity.getYaw()) - MathHelper.lerp(delta, abstractClientPlayerEntity.prevBodyYaw, abstractClientPlayerEntity.bodyYaw);
            float p = MathHelper.lerp(delta, abstractClientPlayerEntity.prevPitch, abstractClientPlayerEntity.getPitch());

            var right = true;
            do {
                right = !right;

                float wobble = (float) (Math.cos(animationProgress / 8 + (right ? Math.PI : 0)) * 0.2 + 0.90);

                matrixStack.push();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(o));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(p));
                matrixStack.translate(1f / 16f * (right ? -4 : 4), 1f / 16f * -8f, 0);
                matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(wobble * (right ? 22 : -22)));
                matrixStack.translate(1f / 16f * (right ? 4 : -4), 1f / 16f * 6f, 1f / 16f * -3f);
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-p));
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-o));
                ((SillyHairsModel) this.getContextModel()).cicada_lib$renderSillyHairs(matrixStack, vertexConsumer, light, overlay, right);
                matrixStack.pop();
            } while (!right);
        }
    }
}
