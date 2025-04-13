package nl.enjarai.cicada.api.conversation.yaml;

import java.util.Map;

public record YamlConversationFile(Map<String, YamlConversation> conversations) {

}
