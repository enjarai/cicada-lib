package nl.enjarai.cicadatest;

import nl.enjarai.cicada.api.conversation.ConversationManager;
import nl.enjarai.cicada.api.util.CicadaEntrypoint;
import nl.enjarai.cicada.api.util.ProperLogger;
import org.slf4j.Logger;

public class CicadaTest implements CicadaEntrypoint {
    public static final String MOD_ID = "cicadatest";
    public static final Logger LOGGER = ProperLogger.getLogger(MOD_ID);

    @Override
    public void registerConversations(ConversationManager conversationManager) {
        conversationManager.registerSourceUrl(
                "https://raw.githubusercontent.com/enjarai/cicada-lib/master/src/testmod/resources/cicada/conversations.json",
                LOGGER::info
        );
    }
}
