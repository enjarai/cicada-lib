package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.Conversation;

public record ModLoadedCondition(String modId) implements LineCondition {
    public static final String TYPE = "cicada:mod_loaded";
    public static final MapCodec<ModLoadedCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("mod_id").forGetter(ModLoadedCondition::modId)
    ).apply(instance, ModLoadedCondition::new));

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean test(Conversation conversation) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
