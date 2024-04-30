package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import nl.enjarai.cicada.api.conversation.Conversation;

import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface LineCondition extends Predicate<Conversation> {
    Map<String, Supplier<MapCodec<? extends LineCondition>>> CODECS = Map.of(
            TrueCondition.TYPE, () -> TrueCondition.CODEC,
            NotCondition.TYPE, () -> NotCondition.CODEC,
            AllCondition.TYPE, () -> AllCondition.CODEC,
            AnyCondition.TYPE, () -> AnyCondition.CODEC,
            ModLoadedCondition.TYPE, () -> ModLoadedCondition.CODEC,
            ModParticipatesCondition.TYPE, () -> ModParticipatesCondition.CODEC,
            AmountOfModsLoadedCondition.TYPE, () -> AmountOfModsLoadedCondition.CODEC,
            PercentageChanceCondition.TYPE, () -> PercentageChanceCondition.CODEC,
            AmountOfModsParticipatingCondition.TYPE, () -> AmountOfModsParticipatingCondition.CODEC,
            DateCondition.TYPE, () -> DateCondition.CODEC
    );
    Codec<LineCondition> CODEC = Codec.STRING.dispatch(LineCondition::getType, type -> CODECS.get(type).get()/*? if <=1.20.4 {*//*.codec()*//*?}*/);

    String getType();
}
