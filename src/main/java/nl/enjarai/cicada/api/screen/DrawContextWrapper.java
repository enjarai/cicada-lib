package nl.enjarai.cicada.api.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class DrawContextWrapper {
    /*? if >=1.20 {*/
    private final net.minecraft.client.gui.DrawContext context;

    public DrawContextWrapper(net.minecraft.client.gui.DrawContext context) {
        this.context = context;
    }

    public MatrixStack matrices() {
        return context.getMatrices();
    }
    /*?} else {*//*
    private final MatrixStack stack;

    public DrawContextWrapper(MatrixStack stack) {
        this.stack = stack;
    }

    public MatrixStack matrices() {
        return stack;
    }
    *//*?}*/
}
