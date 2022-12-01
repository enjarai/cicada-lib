package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.Conversation;

public record IsModLoadedCondition(String modId) implements LineCondition {
    public static final String TYPE = "cicada:is_mod_loaded";
    public static final Codec<IsModLoadedCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("mod_id").forGetter(IsModLoadedCondition::modId)
    ).apply(instance, IsModLoadedCondition::new));

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean test(Conversation conversation) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
