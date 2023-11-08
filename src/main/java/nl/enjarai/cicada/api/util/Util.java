package nl.enjarai.cicada.api.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class Util {
    public static <T, R> Function<T, R> memoize(final Function<T, R> function) {
        return new Function<>() {
            private final Map<T, R> cache = new ConcurrentHashMap<>();

            public R apply(T object) {
                return this.cache.computeIfAbsent(object, function);
            }
        };
    }
}
