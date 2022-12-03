package nl.enjarai.cicada.api.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;

public class IntRange {
    public static final IntRange ANY = new IntRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
    public static final Codec<IntRange> CODEC = Codec.either(Codec.INT, Codec.STRING)
            .xmap(
                    either -> either.map(
                            IntRange::new,
                            IntRange::new
                    ),
                    range -> range.getMin() == range.getMax() ? Either.left(range.getMin()) : Either.right(range.toString())
            );

    private final int min;
    private final int max;

    public IntRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public IntRange(int exactly) {
        this.min = exactly;
        this.max = exactly;
    }

    public IntRange(String range) {
        if (range.equals("*")) {
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
        } else {
            var split = range.split("\\.\\.|-");
            if (split.length == 1) {
                this.min = Integer.parseInt(split[0]);
                this.max = this.min;
            } else {
                this.min = Integer.parseInt(split[0]);
                this.max = Integer.parseInt(split[1]);
            }
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRandom() {
        return (int) (Math.random() * (max - min + 1) + min);
    }

    public boolean isInRange(int value) {
        return value >= min && value <= max;
    }

    @Override
    public String toString() {
        return min + ".." + max;
    }
}
