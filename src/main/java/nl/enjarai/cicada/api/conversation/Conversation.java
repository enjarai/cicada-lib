package nl.enjarai.cicada.api.conversation;

import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import nl.enjarai.cicada.api.util.random.Weighted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Conversation implements Weighted {
    private final ConversationManager conversationManager;

    private final List<LineCondition> conditions = Collections.synchronizedList(new ArrayList<>());
    private final List<Line> lines = Collections.synchronizedList(new ArrayList<>());
    private int priority = 0;
    private int currentOverride = 0;
    private int participantCount = 0;

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

    public void addParticipantCount(int participantCount) {
        this.participantCount += participantCount;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public ConversationManager getConversationManager() {
        return conversationManager;
    }

    public void complete() {
        lines.sort(Comparator.comparingInt(Line::getOrder));
    }

    public boolean shouldRun() {
        return conditions.stream().allMatch(condition -> condition.test(this));
    }
    
    public void run() {
        lines.stream()
                .filter(Line::isConditionMet)
                .forEach(Line::run);
    }

    @Override
    public double getWeight() {
        return getPriority();
    }
}
