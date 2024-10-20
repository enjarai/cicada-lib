package nl.enjarai.cicada.api.util.config;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import nl.enjarai.cicada.api.util.AbstractModConfig;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class CommandConfigInstance<T> {
    private static final Map<String, CommandConfigInstance<?>> commandInstances = new HashMap<>();
    private static final Map<Class<?>, >

    private final String name;
    private final Class<T> type;
    private T configInstance;

    public CommandConfigInstance(String name, T configInstance, Class<T> type) {
        this.name = name;
        this.configInstance = configInstance;
        this.type = type;

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            var rootNode = literal(this.name);

            for (Field field : type.getFields()) {
                if (field.isSynthetic() || Modifier.isTransient(field.getModifiers())) {
                    continue;
                }

                var fieldNode = literal(field.getName());

                fieldNode.

                rootNode.then()
            }
        });
    }

    public static <T extends AbstractModConfig> CommandConfigInstance<T> getOrCreate(String name, T instance, Class<T> type) {
        return commandInstances.computeIfAbsent(name, k -> new CommandConfigInstance<>(name, instance, type)).assumeType(type);
    }

    public static <T extends AbstractModConfig> CommandConfigInstance<T> getOrCreate(String name, T instance) {
        //noinspection unchecked
        return getOrCreate(name, instance, (Class<T>) instance.getClass());
    }

    public <A> CommandConfigInstance<A> assumeType(Class<A> type) {
        if (!type.isAssignableFrom(this.type)) {
            throw new IllegalArgumentException("Type " + type + " is not assignable to " + this.type);
        }
        //noinspection unchecked
        return (CommandConfigInstance<A>) this;
    }
}
