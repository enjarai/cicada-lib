package nl.enjarai.cicada.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Direction;
import nl.enjarai.cicada.util.SillyHairsModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.EnumSet;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin<T extends LivingEntity> extends BipedEntityModel<T> implements SillyHairsModel {
    @Unique
    private ModelPart sillyHairsLeft;
    @Unique
    private ModelPart sillyHairsRight;

    public PlayerEntityModelMixin(ModelPart root) {
        super(root);
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "CONSTANT",
                    args = "stringValue=ear"
            )
    )
    private void initSillyHairs(ModelPart root, boolean thinArms, CallbackInfo ci) {
        sillyHairsLeft = root.getChild("silly_hairs_left");
        sillyHairsRight = root.getChild("silly_hairs_right");
    }

    @Inject(
            method = "getTexturedModelData",
            at = @At(
                    value = "CONSTANT",
                    args = "stringValue=ear"
            )
    )
    private static void textureSillyHairs(Dilation dilation, boolean slim, CallbackInfoReturnable<ModelData> cir, @Local ModelPartData root) {
        root.addChild("silly_hairs_left",
                ModelPartBuilder.create()
                        .uv(-8, -8) // 0 0 top
                        .cuboid(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, EnumSet.of(Direction.EAST)),
                ModelTransform.of(4.0F, -8.0F, -4.0F, 0, 0, -0.5F)
        );
        root.addChild("silly_hairs_right",
                ModelPartBuilder.create()
                        .uv(0, 0) // 0 8 bottom
                        .cuboid(0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, EnumSet.of(Direction.WEST)),
                ModelTransform.of(-4.0F, -8.0F, -4.0F, 0, 0, 0.5F)
        );
    }

    @Override
    public void cicada_lib$renderSillyHairs(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, boolean right, float wobble) {
        var sillyHairs = right ? sillyHairsRight : sillyHairsLeft;

        sillyHairs.roll = wobble;

        sillyHairs.render(matrices, vertices, light, overlay);
    }

    @Inject(
            method = "setVisible",
            at = @At("RETURN")
    )
    private void setSillyHairsVisible(boolean visible, CallbackInfo ci) {
        sillyHairsLeft.visible = visible;
        sillyHairsRight.visible = visible;
    }
}
