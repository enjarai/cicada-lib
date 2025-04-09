package nl.enjarai.cicada.api.conversation.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YamlConversationFile(Map<String, YamlConversation> conversations) {

}
