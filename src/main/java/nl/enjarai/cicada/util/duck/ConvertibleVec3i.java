package nl.enjarai.cicada.util.duck;

import net.minecraft.util.math.Vec3i;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public interface ConvertibleVec3i {
    Vector3i toVector3i();

    Vec3i fromVector3i(Vector3ic vector);
}
