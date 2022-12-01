package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import nl.enjarai.cicada.api.conversation.Conversation;

import java.util.Map;
import java.util.function.Predicate;

public interface LineCondition extends Predicate<Conversation> {
    Map<String, Codec<? extends LineCondition>> CODECS = Map.of(
            TrueCondition.TYPE, TrueCondition.CODEC,
            AllCondition.TYPE, AllCondition.CODEC,
            AnyCondition.TYPE, AnyCondition.CODEC,
            IsModLoadedCondition.TYPE, IsModLoadedCondition.CODEC,
            PercentageChanceCondition.TYPE, PercentageChanceCondition.CODEC
    );
    Codec<LineCondition> CODEC = Codec.STRING.dispatch(LineCondition::getType, CODECS::get);

    String getType();
}
