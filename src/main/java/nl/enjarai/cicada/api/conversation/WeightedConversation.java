package nl.enjarai.cicada.api.conversation;

import nl.enjarai.cicada.api.util.random.Weighted;

public record WeightedConversation(Runnable executor, double weight) implements Weighted {
    @Override
    public double getWeight() {
        return weight;
    }
}
