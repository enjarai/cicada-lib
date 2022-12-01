package nl.enjarai.cicada.api.util;

import nl.enjarai.cicada.api.conversation.ConversationManager;

public interface CicadaEntrypoint {
    /**
     * Use this to register sides of conversations for your mod.
     * <p>
     * May be called outside the main thread, so be careful.
     */
    void registerConversations(ConversationManager conversationManager);
}
