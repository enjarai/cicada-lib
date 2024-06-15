package nl.enjarai.cicadatest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.AbstractModConfig;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.JsonSource;
import nl.enjarai.cicada.api.util.ProperLogger;
import org.slf4j.Logger;

public class CicadaTest implements ModInitializer, CicadaEntrypoint {
    public static final String MOD_ID = "cicadatest";
    public static final Logger LOGGER = ProperLogger.getLogger(MOD_ID);

    @Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSource(
                JsonSource.fromUrl("https://raw.githubusercontent.com/enjarai/cicada-lib/master/src/testmod/resources/cicada/cicada/conversations.json")
                        .or(JsonSource.fromResource("cicada/cicada-test/conversations.json")),
                LOGGER::info
        );
    }

    @Override
    public void onInitialize() {
        var config = AbstractModConfig.loadConfigFile(
                FabricLoader.getInstance().getConfigDir().resolve("cicada-test.json"), new ModConfig());

        LOGGER.warn("testInt: " + config.testInt);
        LOGGER.warn("ayy: " + config.ayy);

        config.testInt += 1;

        LOGGER.warn("testInt: " + config.testInt);

        config.save();

//        var funnyVec = new Vec3d(1, 2, 3);
//        LOGGER.warn("Funny vector " + funnyVec.fromVector3d(funnyVec.toVector3d().mul(2)).getZ());
    }
}
