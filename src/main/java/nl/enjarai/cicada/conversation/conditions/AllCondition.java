package nl.enjarai.cicada.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.conversation.Conversation;

import java.util.List;

public record AllCondition(List<LineCondition> conditions) implements LineCondition {
    public static final String TYPE = "cicada:all";
    public static final Codec<AllCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(LineCondition.CODEC).fieldOf("conditions").forGetter(AllCondition::conditions)
    ).apply(instance, AllCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        return conditions.stream().allMatch(condition -> condition.test(conversation));
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
