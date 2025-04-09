package nl.enjarai.cicada.api.conversation.yaml;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.Logger;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record YamlLine(String mod, String text, List<YamlLine> extend) {
    public void play(Logger logger) {
        if (extend != null && !extend.isEmpty()) {
            if (allRequiredMods()) {
                extend.forEach(line -> line.play(logger));
            }
        } else {
            if (text != null) {
                logger.log(mod, text);
            }
        }
    }

    private boolean allRequiredMods() {
        return extend.stream()
                .map(YamlLine::mod)
                .filter(Objects::nonNull)
                .map(FabricLoader.getInstance()::isModLoaded)
                .reduce(true, Boolean::logicalAnd);
    }
}
