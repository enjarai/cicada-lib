package nl.enjarai.cicada.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;

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
            var helmet = !abstractClientPlayerEntity.getEquippedStack(EquipmentSlot.HEAD).isEmpty();

            var right = true;
            do {
                right = !right;

                float offset = helmet ? 0.1f : 0.45f;
                float wobbleStrength = helmet ? 0.05f : 0.1f;
                float wobble = (float) (Math.cos(animationProgress / 8) * wobbleStrength + (right ? offset : -offset));

                matrixStack.push();
                this.getContextModel().getHead().rotate(matrixStack);

                ((SillyHairsModel) this.getContextModel()).cicada_lib$renderSillyHairs(matrixStack, vertexConsumer, light, overlay, right, wobble);
                matrixStack.pop();
            } while (!right);
        }
    }
}
