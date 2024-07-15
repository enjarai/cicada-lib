package nl.enjarai.cicada.util.duck;

import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.NotImplementedException;
import org.joml.Vector3d;
import org.joml.Vector3dc;

public interface ConvertibleVec3d {
    default Vector3d toVector3d() {
        throw new NotImplementedException("Mixin in cicada broke!! WHaT!?");
    }

    default Vec3d fromVector3d(Vector3dc vector) {
        throw new NotImplementedException("Mixin in cicada broke!! WHaT!?");
    }
}
