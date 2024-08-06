package nl.enjarai.cicada.api.conversation;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.Cicada;
import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.random.RandomUtil;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ConversationManager {
    private static final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(10, new ThreadFactoryBuilder()
            .setNameFormat("Cicada thread %d")
            .setThreadFactory(Executors.defaultThreadFactory())
            .setDaemon(true)
            .build());

    private final Map<JsonSource, Consumer<String>> jsonSources = new HashMap<>();
    private final Map<String, Conversation> conversations = new ConcurrentHashMap<>();

    public static ExecutorService getThreadPool() {
        return THREAD_POOL;
    }

    public void init() {
        FabricLoader.getInstance().getEntrypoints("cicada", CicadaEntrypoint.class)
                .forEach(entrypoint -> entrypoint.registerConversations(this));

        // Concurrently loads all the conversations, getting them from their sources and decoding the json.
        getConversationsFuture(jsonSources).join();

        conversations.values().forEach(Conversation::complete);
    }

    public void run() {
        RandomUtil.chooseWeighted(conversations.values().stream()
                        .filter(Conversation::shouldRun)
                        .toList())
                .ifPresent(Conversation::run);
    }

    public void registerSource(JsonSource source, Consumer<String> logger) {
        jsonSources.put(source, logger);
    }

    @Deprecated
    public void registerUrlSource(String url, Consumer<String> logger) {
        registerSource(JsonSource.fromUrl(url), logger);
    }

    @Deprecated
    public void registerFileSource(Path path, Consumer<String> logger) {
        registerSource(JsonSource.fromFile(path), logger);
    }

    public Conversation getOrCreateConversation(String id) {
        var conversation = conversations.get(id);
        if (conversation == null) {
            conversation = new Conversation(this);
            conversations.put(id, conversation);
        }
        return conversation;
    }

    protected void onLoadError(Exception e) {
        Cicada.LOGGER.debug("Failed to load conversation source", e);
    }

    private CompletableFuture<Void> getConversationsFuture(Map<JsonSource, Consumer<String>> sources) {
        return CompletableFuture.allOf(sources.entrySet().stream()
                .map(entry -> CompletableFuture.runAsync(
                        () -> entry.getKey().getSafely(this::onLoadError)
                                .ifPresent(json -> {
                                    try {
                                        decodeSideJson(json, line -> {
                                            if (line instanceof SimpleLine simpleLine) {
                                                simpleLine.setSourceLogger(entry.getValue());
                                            }
                                        });
                                    } catch (Exception e) {
                                        onLoadError(e);
                                    }
                                }),
                        THREAD_POOL
                )).toArray(CompletableFuture[]::new)
        );
    }

    private void decodeSideJson(JsonObject json, Consumer<Line> lineModifier) {
        String modId;
        if (json.has("mod_id")) {
            modId = json.get("mod_id").getAsString();
        } else {
            modId = null;
        }

        json.getAsJsonObject("conversations").entrySet().forEach(entry -> {
            var conversation = getOrCreateConversation(entry.getKey());
            var conversationJson = entry.getValue().getAsJsonObject();

            if (conversationJson.has("condition")) {
                conversation.addCondition(LineCondition.CODEC.parse(JsonOps.INSTANCE, conversationJson.get("condition"))
                        /*? if >=1.20.5 {*/
                        .getOrThrow());
                        /*?} else {*/
                        /*.getOrThrow(false, string -> {}));
                        *//*?}*/
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
                        /*? if >=1.20.5 {*/
                        .getOrThrow();
                        /*?} else {*/
                        /*.getOrThrow(false, string -> {});
                        *//*?}*/

                line.setConversation(conversation);
                lineModifier.accept(line);
                conversation.addLine(line);
            });

            conversation.addParticipantCount(1);
            conversation.addParticipant(modId);
        });
    }
}
