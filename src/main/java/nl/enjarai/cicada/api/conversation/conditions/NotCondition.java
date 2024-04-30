package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.Conversation;

public record NotCondition(LineCondition condition) implements LineCondition {
    public static final String TYPE = "cicada:not";
    public static final MapCodec<NotCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LineCondition.CODEC.fieldOf("condition").forGetter(NotCondition::condition)
    ).apply(instance, NotCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        return !condition.test(conversation);
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
