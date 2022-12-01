package nl.enjarai.cicada.api.conversation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import nl.enjarai.cicada.api.conversation.conditions.TrueCondition;

import java.util.function.Consumer;

public class SimpleLine implements Line {
    public static final Codec<SimpleLine> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("order").forGetter(SimpleLine::getOrder),
            LineCondition.CODEC.optionalFieldOf("condition", new TrueCondition()).forGetter(SimpleLine::getCondition),
            Codec.STRING.fieldOf("text").forGetter(SimpleLine::getText)
    ).apply(instance, SimpleLine::new));

    private Conversation conversation;
    private Consumer<String> sourceLogger;

    private final int order;
    private final LineCondition condition;
    private final String text;

    public SimpleLine(int order, LineCondition condition, String text) {
        this.order = order;
        this.condition = condition;
        this.text = text;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
    }

    public void setSourceLogger(Consumer<String> sourceLogger) {
        this.sourceLogger = sourceLogger;
    }

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public boolean isConditionMet() {
        return condition.test(conversation);
    }

    protected LineCondition getCondition() {
        return condition;
    }

    @Override
    public String getText() {
        return text;
    }
}
