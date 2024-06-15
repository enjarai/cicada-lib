package nl.enjarai.cicada.mixin;

import net.minecraft.util.math.Vec3d;
import nl.enjarai.cicada.util.duck.ConvertibleVec3d;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = Vec3d.class, priority = 500)
public class Vec3dMixin implements ConvertibleVec3d {
    @Shadow @Final @Mutable
    public double x;
    @Shadow @Final @Mutable
    public double y;
    @Shadow @Final @Mutable
    public double z;

    @Override
    public Vector3d toVector3d() {
        return new Vector3d(x, y, z);
    }

    @Override
    public Vec3d fromVector3d(Vector3dc vector) {
        x = vector.x();
        y = vector.y();
        z = vector.z();
        return (Vec3d) (Object) this;
    }
}
