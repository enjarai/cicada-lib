package nl.enjarai.cicada.api.render;

import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.util.duck.KeyableRenderState;

public class RenderStateKey<T> {
    private final Identifier id;
    private final T defaultValue;

    private RenderStateKey(Identifier id, T defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    public static <T> RenderStateKey<T> of(Identifier id, T defaultValue) {
        return new RenderStateKey<>(id, defaultValue);
    }

    @SuppressWarnings("unchecked")
    public T get(EntityRenderState state) {
        return (T) ((KeyableRenderState) state).cicada$getStateMap().getOrDefault(id, defaultValue);
    }

    public void put(EntityRenderState state, T value) {
        ((KeyableRenderState) state).cicada$getStateMap().put(id, value);
    }
}
