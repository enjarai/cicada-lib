package nl.enjarai.cicada.mixin;

import net.minecraft.client.render.entity.state.EntityRenderState;
import nl.enjarai.cicada.util.duck.KeyableRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements KeyableRenderState {
    @Unique
    private final Map<Identifier, Object> stateMap = new HashMap<>();

    @Override
    public Map<Identifier, Object> cicada$getStateMap() {
        return stateMap;
    }
}
