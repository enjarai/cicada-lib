package nl.enjarai.cicada.conversation;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import nl.enjarai.cicada.conversation.conditions.LineCondition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ConversationManager {
    private static final Gson GSON = new Gson();

    private final Map<String, Consumer<String>> sourceUrls = new HashMap<>();
    private final Map<String, Conversation> conversations = new HashMap<>();

    public void init() {
        // use lineModifier to set the conversationmanager and sourcelogger
    }

    private JsonObject downloadJson(String url) throws IOException {
        URLConnection conn = new URL(url).openConnection();
        conn.connect();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            return GSON.fromJson(in, JsonObject.class);
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
            conversationJson.getAsJsonArray("lines").forEach(jsonElement -> {
                var lineJson = jsonElement.getAsJsonObject();
                var line = SimpleLine.CODEC.parse(JsonOps.INSTANCE, lineJson)
                        .getOrThrow(false, string -> {});

                lineModifier.accept(line);
                conversation.addLine(line);
            });
        });
    }

    public void registerConversationListUrl(String url, Consumer<String> logger) {
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
