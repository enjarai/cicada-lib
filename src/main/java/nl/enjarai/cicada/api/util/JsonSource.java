package nl.enjarai.cicada.api.util;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A source of json data to be decoded. May return an {@link Optional#empty()} or
 * throw an {@link IOException}, {@link JsonSyntaxException} or {@link JsonIOException}.
 * <p>
 *     Several static methods are provided to create a JsonSource from a file, url or string.
 * </p>
 */
public interface JsonSource {
    JsonSource EMPTY = Optional::empty;

    /**
     * Creates a JsonSource from a String, attempting to parse it as json.
     */
    static JsonSource fromString(String string) {
        return () -> Optional.of(new Gson().fromJson(string, JsonObject.class));
    }

    /**
     * Creates a JsonSource from a file path given as a string, shortcut for {@link #fromFile(Path)}.
     */
    static JsonSource fromFile(String path) {
        return fromFile(Path.of(path));
    }

    /**
     * Creates a JsonSource from a file at the given {@link Path}.
     */
    static JsonSource fromFile(Path path) {
        return () -> {
            try (BufferedReader in = Files.newBufferedReader(path)) {
                return Optional.of(new Gson().fromJson(in, JsonObject.class));
            }
        };
    }

    /**
     * Creates a JsonSource from a valid URL in String form. Ensuring a proper connection is created and closed.
     * <p>
     *     If the URL is invalid, this will return an {@link JsonSource#EMPTY} JsonSource.
     *     If you need to ensure a valid source, use {@link #fromUrl(URL)}.
     * </p>
     */
    static JsonSource fromUrl(String url) {
        try {
            return fromUrl(new URL(url));
        } catch (MalformedURLException ignored) {
            return EMPTY;
        }
    }

    /**
     * Creates a JsonSource from a valid URL. Ensuring a proper connection is created and closed.
     * <p>
     *     The alternative {@link #fromUrl(String)} is available if you
     *     don't want to handle {@link MalformedURLException}s yourself.
     * </p>
     */
    static JsonSource fromUrl(URL url) {
        return () -> {
            URLConnection conn = url.openConnection();
            conn.connect();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return Optional.of(new Gson().fromJson(in, JsonObject.class));
            }
        };
    }

    /**
     * Creates a JsonSource from a resource in the classpath. The resource is loaded using the
     * {@link ClassLoader#getResourceAsStream(String)} method.
     */
    static JsonSource fromResource(String path) {
        return () -> {
            var resourceStream = JsonSource.class.getClassLoader().getResourceAsStream(path);
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + path);
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(resourceStream))) {
                return Optional.of(new Gson().fromJson(in, JsonObject.class));
            }
        };
    }

    /**
     * Gets the json data from this source. Returns an {@link Optional} containing the json data,
     * or {@link Optional#empty()} if the source is empty. May throw an {@link IOException},
     * {@link JsonSyntaxException} or {@link JsonIOException} in the process.
     */
    Optional<JsonObject> get() throws IOException, JsonSyntaxException, JsonIOException;

    /**
     * Sources this json, but returns an {@link Optional#empty()} if an {@link IOException},
     * {@link JsonSyntaxException} or {@link JsonIOException} is thrown in the process.
     * <p>
     *     Also accepts a {@link Consumer} that will be called with the exception if one is thrown.
     * </p>
     */
    default Optional<JsonObject> getSafely(Consumer<Exception> errorHandler) {
        try {
            return get();
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            errorHandler.accept(e);
            return Optional.empty();
        }
    }

    /**
     * Sources this json, but returns an {@link Optional#empty()} if an {@link IOException},
     * {@link JsonSyntaxException} or {@link JsonIOException} is thrown in the process.
     */
    default Optional<JsonObject> getSilently() {
        return getSafely(e -> {});
    }

    /**
     * Combines this JsonSource with another, using the other if this returns empty or throws an exception.
     * <p>
     *     This ignores all exceptions thrown by this JsonSource,
     *     any thrown by the other JsonSource will be passed on however.
     * </p>
     */
    default JsonSource or(JsonSource other) {
        return () -> {
            var thisJson = getSilently();
            return thisJson.isPresent() ? thisJson : other.get();
        };
    }
}
