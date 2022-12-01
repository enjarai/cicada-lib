package nl.enjarai.cicada.api.conversation;

import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Conversation implements Comparable<Conversation> {
    private final ConversationManager conversationManager;

    private final List<LineCondition> conditions = new ArrayList<>();
    private final List<Line> lines = new ArrayList<>();
    private int priority = 100;
    private int currentOverride = 0;

    public Conversation(ConversationManager conversationManager) {
        this.conversationManager = conversationManager;
    }

    public void addCondition(LineCondition condition) {
        conditions.add(condition);
    }

    public void addLine(Line line) {
        lines.add(line);
    }

    public void addPriority(int priority, int override) {
        if (override > currentOverride) {
            this.priority = priority;
            this.currentOverride = override;
        }
    }

    public int getPriority() {
        return priority;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public void complete() {
        lines.sort((line1, line2) -> Integer.compare(line2.getOrder(), line1.getOrder()));
    }

    public boolean shouldRun() {
        return conditions.stream().allMatch(condition -> condition.test(this));
    }
    
    public void run() {
    }

    @Override
    public int compareTo(@NotNull Conversation o) {
        return Integer.compare(getPriority(), o.getPriority());
    }
}
