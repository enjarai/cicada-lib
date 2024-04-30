package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.MapCodec;
import nl.enjarai.cicada.api.conversation.Conversation;

import java.util.List;

public record AnyCondition(List<LineCondition> conditions) implements LineCondition {
    public static final String TYPE = "cicada:any";
    public static final MapCodec<AnyCondition> CODEC = AllCondition.CODEC.xmap(
            allCondition -> new AnyCondition(allCondition.conditions()),
            anyCondition -> new AllCondition(anyCondition.conditions())
    );

    @Override
    public boolean test(Conversation conversation) {
        return conditions.stream().anyMatch(condition -> condition.test(conversation));
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
