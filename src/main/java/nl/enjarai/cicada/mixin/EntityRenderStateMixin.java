package nl.enjarai.cicada.mixin;

import net.minecraft.client.MinecraftClient;
/*? if >=1.21.2 {*/
import net.minecraft.client.render.entity.state.EntityRenderState;
/*?}*/
import nl.enjarai.cicada.util.duck.KeyableRenderState;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

/*? if >=1.21.2 {*/
@Mixin(EntityRenderState.class)
/*?} else {*//*
// Dummy target for older versions
@Mixin(MinecraftClient.class)
*//*?}*/
public class EntityRenderStateMixin implements KeyableRenderState {
    /*? if >=1.21.2 {*/
    @Unique
    private final Map<Identifier, Object> stateMap = new HashMap<>();

    @Override
    public Map<Identifier, Object> cicada$getStateMap() {
        return stateMap;
    }
    /*?} else {*//*
    @Override
    public Map<Identifier, Object> cicada$getStateMap() {
        return Map.of();
    }
    *//*?}*/
}
