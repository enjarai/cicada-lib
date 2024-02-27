package nl.enjarai.cicada.util;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public interface SillyHairsModel {
    void cicada_lib$renderSillyHairs(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, boolean right);
}
