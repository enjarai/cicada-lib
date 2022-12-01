package nl.enjarai.cicada.conversation;

import nl.enjarai.cicada.conversation.conditions.LineCondition;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private final ConversationManager conversationManager;

    private final List<LineCondition> conditions = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();

    public Conversation(ConversationManager conversationManager) {
        this.conversationManager = conversationManager;
    }

    public void addCondition(LineCondition condition) {
        conditions.add(condition);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void complete() {
        lines.sort((line1, line2) -> Integer.compare(line2.getPriority(), line1.getPriority()));
    }
}
