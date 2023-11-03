package nl.enjarai.cicada.util;

import com.google.gson.JsonParseException;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import nl.enjarai.cicada.Cicada;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Significant portions of this code have been adapted from the Capes mod by CaelTheColher:
// https://github.com/CaelTheColher/Capes/blob/architectury/common/src/main/kotlin/me/cael/capes/handler/PlayerHandler.kt
// As such, this file is licensed under LGPL-3, unlike the rest of the project.
// https://www.gnu.org/licenses/lgpl-3.0.en.html
@Environment(EnvType.CLIENT)
public final class CapeHandler {
    private static final String META_URL = "https://enjarai.dev/cicada-lib/meta/capes/%s/meta.json";
    private static final String CAPE_URL = "https://enjarai.dev/cicada-lib/meta/capes/%s/cape.png";

    private static final ExecutorService capeExecutor = Executors.newFixedThreadPool(2);
    private static final HashMap<UUID, CapeHandler> instances = new HashMap<>();

    public static void shutdown() {
        capeExecutor.shutdownNow();
    }

    public static CapeHandler fromProfile(GameProfile profile) {
        return instances.computeIfAbsent(profile.getId(), uuid -> new CapeHandler(profile));
    }

    public static void onLoadTexture(GameProfile profile) {
        var handler = fromProfile(profile);
        capeExecutor.submit(handler::loadCape);
    }

    private static HttpURLConnection getConnection(String url) throws IOException {
        var connection = (HttpURLConnection) new URL(url).openConnection(MinecraftClient.getInstance().getNetworkProxy());
        connection.setRequestProperty("User-Agent", "Mozilla/4.0");
        connection.setDoInput(true);
        connection.setDoOutput(false);
        return connection;
    }


    public CapeHandler(GameProfile profile) {
        uuid = profile.getId();
    }

    private final UUID uuid;
    private boolean hasCape = false;
    private boolean hasElytra = false;

    public boolean hasCape() {
        return hasCape;
    }

    public boolean hasElytra() {
        return hasElytra;
    }

    public Identifier getCapeTexture() {
        return Cicada.id(uuid.toString());
    }

    private void loadCape() {
        try {
            var metaUrl = String.format(META_URL, uuid);
            var metaConnection = getConnection(metaUrl);
            var meta = CapeMeta.fromJson(metaConnection.getInputStream());

            if (meta.cape() || meta.elytra()) {
                var capeUrl = String.format(CAPE_URL, uuid);
                var capeConnection = getConnection(capeUrl);
                setCapeTexture(capeConnection.getInputStream(), meta);
            }
        } catch (IOException | JsonParseException ignored) {
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public boolean setCapeTexture(InputStream image, CapeMeta meta) {
        try {
            var cape = NativeImage.read(image);
            MinecraftClient.getInstance().execute(() -> {
                MinecraftClient.getInstance().getTextureManager().registerTexture(
                        getCapeTexture(), new NativeImageBackedTexture(parseCape(cape))
                );
                hasCape = meta.cape();
                hasElytra = meta.elytra();
            });
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private NativeImage parseCape(NativeImage img) {
        var imageWidth = 64;
        var imageHeight = 32;
        var srcWidth = img.getWidth();
        var srcHeight= img.getHeight();
        while (imageWidth < srcWidth || imageHeight < srcHeight) {
            imageWidth *= 2;
            imageHeight *= 2;
        }
        var imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < srcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                imgNew.setColor(x, y, img.getColor(x, y));
            }
        }
        img.close();
        return imgNew;
    }
}
