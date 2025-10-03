package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.MapCodec;
import nl.enjarai.cicada.api.conversation.Conversation;

public record TrueCondition() implements LineCondition {
    public static final String TYPE = "cicada:true";
    public static final MapCodec<TrueCondition> CODEC = MapCodec.unit(TrueCondition::new);

    @Override
    public boolean test(Conversation conversation) {
        return true;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
