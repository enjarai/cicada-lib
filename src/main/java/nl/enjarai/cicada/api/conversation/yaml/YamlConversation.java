package nl.enjarai.cicada.api.conversation.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.Logger;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public record YamlConversation(List<YamlLine> lines, double priority) {
    public void play(Logger logger) {
        lines.forEach(line -> line.play(logger));
    }

    public boolean allRequiredMods() {
        return lines.stream()
                .map(YamlLine::mod)
                .filter(Objects::nonNull)
                .map(FabricLoader.getInstance()::isModLoaded)
                .reduce(true, Boolean::logicalAnd);
    }
}
