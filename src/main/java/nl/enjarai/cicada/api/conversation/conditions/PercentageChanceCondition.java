package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.Conversation;

public record PercentageChanceCondition(int chance) implements LineCondition {
    public static final String TYPE = "cicada:percentage_chance";
    public static final Codec<PercentageChanceCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("chance").forGetter(PercentageChanceCondition::chance)
    ).apply(instance, PercentageChanceCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        return Math.random() * 100 < chance;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
