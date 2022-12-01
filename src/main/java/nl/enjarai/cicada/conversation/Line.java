package nl.enjarai.cicada.conversation;

public interface Line {
    int getPriority();

    boolean isConditionMet();

    String getText();
}
