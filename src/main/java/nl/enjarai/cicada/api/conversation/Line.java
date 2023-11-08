package nl.enjarai.cicada.api.conversation;

import java.util.Optional;

public interface Line {
    int getOrder();

    boolean isConditionMet();

    String getText();

    default Optional<String> getAuthorOverride() {
        return Optional.empty();
    }

    void run();
}
