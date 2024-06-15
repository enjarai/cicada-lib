package nl.enjarai.cicada.mixin;

import net.minecraft.util.math.Vec3i;
import nl.enjarai.cicada.util.duck.ConvertibleVec3i;
import org.joml.Vector3i;
import org.joml.Vector3ic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@SuppressWarnings("AddedMixinMembersNamePattern")
@Mixin(value = Vec3i.class, priority = 500)
public class Vec3iMixin implements ConvertibleVec3i {
    @Shadow private int x;
    @Shadow private int y;
    @Shadow private int z;

    @Override
    public Vector3i toVector3i() {
        return new Vector3i(x, y, z);
    }

    @Override
    public Vec3i fromVector3i(Vector3ic vector) {
        x = vector.x();
        y = vector.y();
        z = vector.z();
        return (Vec3i) (Object) this;
    }
}
