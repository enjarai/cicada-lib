package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.Conversation;

public record ModParticipatesCondition(String modId) implements LineCondition {
    public static final String TYPE = "cicada:mod_participates";
    public static final MapCodec<ModParticipatesCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("mod_id").forGetter(ModParticipatesCondition::modId)
    ).apply(instance, ModParticipatesCondition::new));

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean test(Conversation conversation) {
        return conversation.getParticipants().contains(modId);
    }
}
