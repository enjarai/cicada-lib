package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.Conversation;
import nl.enjarai.cicada.api.util.IntRange;

public record AmountOfModsLoadedCondition(IntRange count) implements LineCondition {
    public static final String TYPE = "cicada:amount_of_mods_loaded";
    public static final Codec<AmountOfModsLoadedCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntRange.CODEC.fieldOf("count").forGetter(AmountOfModsLoadedCondition::count)
    ).apply(instance, AmountOfModsLoadedCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        return count.isInRange(FabricLoader.getInstance().getAllMods().size());
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
