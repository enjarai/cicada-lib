package nl.enjarai.cicada.conversation.conditions;

import com.mojang.serialization.Codec;
import nl.enjarai.cicada.conversation.Conversation;

public record TrueCondition() implements LineCondition {
    public static final String TYPE = "cicada:true";
    public static final Codec<TrueCondition> CODEC = Codec.unit(TrueCondition::new);

    @Override
    public boolean test(Conversation conversation) {
        return true;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
