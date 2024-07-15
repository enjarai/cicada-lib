package nl.enjarai.cicada.util.duck;

import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.NotImplementedException;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public interface ConvertibleVec3i {
    default Vector3i toVector3i() {
        throw new NotImplementedException("Mixin in cicada broke!! WHaT!?");
    }

    default Vec3i fromVector3i(Vector3ic vector) {
        throw new NotImplementedException("Mixin in cicada broke!! WHaT!?");
    }
}
