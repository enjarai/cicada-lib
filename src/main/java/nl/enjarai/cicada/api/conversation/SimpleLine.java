package nl.enjarai.cicada.api.conversation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.conditions.LineCondition;
import nl.enjarai.cicada.api.conversation.conditions.TrueCondition;
import nl.enjarai.cicada.api.util.ProperLogger;
import nl.enjarai.cicada.api.util.Util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SimpleLine implements Line {
    public static final Function<String, Consumer<String>> DEFAULT_LOGGER = Util.memoize(
            ((Function<String, ProperLogger>) ProperLogger::getLogger).andThen(logger -> logger::info));
    public static final Codec<SimpleLine> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("order").forGetter(SimpleLine::getOrder),
            LineCondition.CODEC.optionalFieldOf("condition", new TrueCondition()).forGetter(SimpleLine::getCondition),
            Codec.STRING.fieldOf("text").forGetter(SimpleLine::getText),
            Codec.STRING.optionalFieldOf("author_override").forGetter(SimpleLine::getAuthorOverride)
    ).apply(instance, SimpleLine::new));

    private Conversation conversation;
    private Consumer<String> sourceLogger;

    private final int order;
    private final LineCondition condition;
    private final String text;
    private final Optional<String> authorOverride;

    public SimpleLine(int order, LineCondition condition, String text, Optional<String> authorOverride) {
        this.order = order;
        this.condition = condition;
        this.text = text;
        this.authorOverride = authorOverride;
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

    @Override
    public Optional<String> getAuthorOverride() {
        return authorOverride;
    }

    public void run() {
        getAuthorOverride().ifPresentOrElse(
                s -> DEFAULT_LOGGER.apply(s).accept(text),
                () -> sourceLogger.accept(text)
        );
    }
}
