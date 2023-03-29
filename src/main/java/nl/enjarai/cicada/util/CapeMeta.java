package nl.enjarai.cicada.util;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("unused")
public final class CapeMeta {
    private boolean cape;
    private boolean elytra;

    public static CapeMeta fromJson(InputStream inputStream) {
        var reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new Gson().fromJson(reader, CapeMeta.class);
    }

    public boolean cape() {
        return cape;
    }

    public boolean elytra() {
        return elytra;
    }
}
