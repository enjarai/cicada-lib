package nl.enjarai.cicada.api.util;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A source of yaml data to be decoded. May return an {@link Optional#empty()} or
 * throw an {@link IOException}, {@link JsonSyntaxException} or {@link JsonIOException}.
 * <p>
 *     Several static methods are provided to create a YamlSource from a file, url or string.
 * </p>
 */
public interface YamlSource {
    YamlSource EMPTY = Optional::empty;
    Yaml YAML = new Yaml();

    /**
     * Creates a YamlSource from a String, attempting to parse it as json.
     */
    static YamlSource fromString(String string) {
        return () -> Optional.of(YAML.load(string));
    }

    /**
     * Creates a YamlSource from a file path given as a string, shortcut for {@link #fromFile(Path)}.
     */
    static YamlSource fromFile(String path) {
        return fromFile(Path.of(path));
    }

    /**
     * Creates a YamlSource from a file at the given {@link Path}.
     */
    static YamlSource fromFile(Path path) {
        return () -> {
            try (BufferedReader in = Files.newBufferedReader(path)) {
                return Optional.of(YAML.load(in));
            }
        };
    }

    /**
     * Creates a YamlSource from a valid URL in String form. Ensuring a proper connection is created and closed.
     * <p>
     *     If the URL is invalid, this will return an {@link YamlSource#EMPTY} YamlSource.
     *     If you need to ensure a valid source, use {@link #fromUrl(URL)}.
     * </p>
     */
    static YamlSource fromUrl(String url) {
        try {
            return fromUrl(new URL(url));
        } catch (MalformedURLException ignored) {
            return EMPTY;
        }
    }

    /**
     * Creates a YamlSource from a valid URL. Ensuring a proper connection is created and closed.
     * <p>
     *     The alternative {@link #fromUrl(String)} is available if you
     *     don't want to handle {@link MalformedURLException}s yourself.
     * </p>
     */
    static YamlSource fromUrl(URL url) {
        return () -> {
            URLConnection conn = url.openConnection();
            conn.connect();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return Optional.of(YAML.load(in));
            }
        };
    }

    /**
     * Creates a YamlSource from a resource in the classpath. The resource is loaded using the
     * {@link ClassLoader#getResource(String)} method.
     */
    static YamlSource fromResource(String path) {
        return () -> {
            var resourceStream = JsonSource.class.getClassLoader().getResourceAsStream(path);
            if (resourceStream == null) {
                throw new IOException("Resource not found: " + path);
            }
            try (BufferedReader in = new BufferedReader(new InputStreamReader(resourceStream))) {
                return Optional.of(YAML.load(in));
            }
        };
    }

    /**
     * Gets the yaml data from this source. Returns an {@link Optional} containing the yaml data,
     * or {@link Optional#empty()} if the source is empty. May throw an in the process.
     */
    Optional<Map<Object, Object>> get() throws IOException;

    /**
     * Sources this yaml, but returns an {@link Optional#empty()} if an {@link IOException} is thrown in the process.
     * <p>
     *     Also accepts a {@link Consumer} that will be called with the exception if one is thrown.
     * </p>
     */
    default Optional<Map<Object, Object>> getSafely(Consumer<Exception> errorHandler) {
        try {
            return get();
        } catch (IOException | JsonSyntaxException | JsonIOException e) {
            errorHandler.accept(e);
            return Optional.empty();
        }
    }

    /**
     * Sources this yaml, but returns an {@link Optional#empty()} if an {@link IOException} is thrown in the process.
     */
    default Optional<Map<Object, Object>> getSilently() {
        return getSafely(e -> {});
    }

    /**
     * Combines this YamlSource with another, using the other if this returns empty or throws an exception.
     * <p>
     *     This ignores all exceptions thrown by this YamlSource,
     *     any thrown by the other YamlSource will be passed on however.
     * </p>
     */
    default YamlSource or(YamlSource other) {
        return () -> {
            var thisYaml = getSilently();
            return thisYaml.isPresent() ? thisYaml : other.get();
        };
    }
}
