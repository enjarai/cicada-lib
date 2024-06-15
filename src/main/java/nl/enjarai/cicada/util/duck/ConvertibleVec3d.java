package nl.enjarai.cicada.util.duck;

import net.minecraft.util.math.Vec3d;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public interface ConvertibleVec3d {
    Vector3d toVector3d();

    Vec3d fromVector3d(Vector3dc vector);
}
