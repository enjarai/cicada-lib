package nl.enjarai.cicada.api.render;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.entity.Entity;

import java.util.HashMap;

public class RenderStateUpdateEvent {
    private static final HashMap<Class<? extends Entity>, Event<UpdateEvent<Entity>>> updateEvents = new HashMap<>();

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Entity> Event<UpdateEvent<T>> get(Class<T> tClass) {
        return (Event<UpdateEvent<T>>) (Event) updateEvents.computeIfAbsent(tClass, c ->
                EventFactory.createArrayBacked(UpdateEvent.class, events -> (entity, state, tickDelta) -> {
                    if (tClass.isInstance(entity)) {
                        for (UpdateEvent event : events) {
                            event.onStateUpdate(tClass.cast(entity), state, tickDelta);
                        }
                    }
                }));
    }

    public static UpdateEvent<Entity> allInvoker() {
        return RenderStateUpdateEvent::invokeAll;
    }

    private static void invokeAll(Entity entity, EntityRenderState state, float tickDelta) {
        for (var value : updateEvents.values()) {
            value.invoker().onStateUpdate(entity, state, tickDelta);
        }
    }

    public interface UpdateEvent<T extends Entity> {
        void onStateUpdate(T entity, EntityRenderState state, float tickDelta);
    }
}
