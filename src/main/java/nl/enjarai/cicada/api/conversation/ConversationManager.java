package nl.enjarai.cicada.api.conversation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.Cicada;
import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.RandomUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ConversationManager {
    private static final Gson GSON = new Gson();

    private final Map<String, Consumer<String>> sourceUrls = new HashMap<>();
    private final Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    public void init() {
        FabricLoader.getInstance().getEntrypoints("cicada", CicadaEntrypoint.class)
                .forEach(entrypoint -> entrypoint.registerConversations(this));

        CompletableFuture.allOf(sourceUrls.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(() -> downloadJson(entry.getKey())
                        .ifPresent(json -> decodeSideJson(json, line -> {
                            if (line instanceof SimpleLine simpleLine) {
                                simpleLine.setSourceLogger(entry.getValue());
                            }
                        }))
                )).toArray(CompletableFuture[]::new)
        ).join();

        conversations.values().forEach(Conversation::complete);
    }

    public void run() {
        RandomUtil.chooseWeighted(conversations.values().stream()
                .filter(Conversation::shouldRun)
                .toList())
                .ifPresent(Conversation::run);
    }

    protected void onDownloadError(IOException e) {
        Cicada.LOGGER.debug("Failed to download conversation source", e);
    }

    private Optional<JsonObject> downloadJson(String url) {
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                return Optional.of(GSON.fromJson(in, JsonObject.class));
            }
        } catch (IOException e) {
            onDownloadError(e);
            return Optional.empty();
        }
    }

    private void decodeSideJson(JsonObject json, Consumer<Line> lineModifier) {
        json.getAsJsonObject("conversations").entrySet().forEach(entry -> {
            var conversation = getOrCreateConversation(entry.getKey());
            var conversationJson = entry.getValue().getAsJsonObject();

            if (conversationJson.has("condition")) {
                conversation.addCondition(LineCondition.CODEC.parse(JsonOps.INSTANCE, conversationJson.get("condition"))
                        .getOrThrow(false, string -> {}));
            }
            if (conversationJson.has("priority")) {
                var priority = conversationJson.getAsJsonObject("priority");

                var convoPriority = priority.get("priority").getAsInt();
                var convoPriorityOverride = priority.has("override") ?
                        priority.get("override").getAsInt() : 100;

                conversation.addPriority(convoPriority, convoPriorityOverride);
            }
            conversationJson.getAsJsonArray("lines").forEach(jsonElement -> {
                var lineJson = jsonElement.getAsJsonObject();
                var line = SimpleLine.CODEC.parse(JsonOps.INSTANCE, lineJson)
                        .getOrThrow(false, string -> {
                        });

                line.setConversation(conversation);
                lineModifier.accept(line);
                conversation.addLine(line);
            });
        });
    }

    public void registerSourceUrl(String url, Consumer<String> logger) {
        sourceUrls.put(url, logger);
    }

    public Conversation getOrCreateConversation(String id) {
        var conversation = conversations.get(id);
        if (conversation == null) {
            conversation = new Conversation(this);
            conversations.put(id, conversation);
        }
        return conversation;
    }
}
