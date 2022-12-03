package nl.enjarai.cicada.api.util.random;

import java.util.Collection;
import java.util.Optional;

public class RandomUtil {
    public static <T extends Weighted> Optional<T> chooseWeighted(Collection<T> items) {
        double completeWeight = 0.0;
        for (T item : items) {
            completeWeight += item.getWeight();
        }
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (T item : items) {
            countWeight += item.getWeight();
            if (countWeight >= r) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
