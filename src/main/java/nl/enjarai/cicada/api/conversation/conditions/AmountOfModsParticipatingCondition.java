package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.Conversation;
import nl.enjarai.cicada.api.util.IntRange;

public record AmountOfModsParticipatingCondition(IntRange count) implements LineCondition {
    public static final String TYPE = "cicada:amount_of_mods_participating";
    public static final MapCodec<AmountOfModsParticipatingCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntRange.CODEC.fieldOf("count").forGetter(AmountOfModsParticipatingCondition::count)
    ).apply(instance, AmountOfModsParticipatingCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        return count.isInRange(conversation.getParticipantCount());
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
