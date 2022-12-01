package nl.enjarai.cicada.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Language;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

// Shamelessly robbed from better-lang-reader by geekazodium.
// See: https://github.com/geekazodium/better-lang-reader/blob/main/src/main/java/com/geekazodium/betterlangreader/mixins/BetterLanguageParsing.java
@Mixin(Language.class)
public abstract class LanguageMixin {
    @Shadow
    @Final
    private static Pattern TOKEN_PATTERN;

    @Inject(
            method = "load",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void cicada$betterLangLoadingMethod(InputStream inputStream, BiConsumer<String, String> entryConsumer, CallbackInfo ci) {
        JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject();
        loadFromObject(jsonObject, entryConsumer);
        ci.cancel();
    }

    private static void loadFromObject(JsonObject object, BiConsumer<String, String> entryConsumer) {
        loadFromObject(object, entryConsumer, "");
    }

    private static void loadFromObject(JsonObject object, BiConsumer<String, String> entryConsumer, String s) {
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            String key;

            if (s.length() > 0) {
                key = s + "." + entry.getKey();
            } else {
                key = entry.getKey();
            }

            if (entry.getValue().isJsonObject()) {
                loadFromObject(entry.getValue().getAsJsonObject(), entryConsumer, key);
            } else {
                String string = TOKEN_PATTERN
                        .matcher(JsonHelper.asString(entry.getValue(), key))
                        .replaceAll("%$1s");
                entryConsumer.accept(key, string);
            }
        }
    }
}
