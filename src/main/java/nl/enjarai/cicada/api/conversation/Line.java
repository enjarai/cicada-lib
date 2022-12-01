package nl.enjarai.cicada.api.conversation;

public interface Line {
    int getOrder();

    boolean isConditionMet();

    String getText();

    void run();
}
