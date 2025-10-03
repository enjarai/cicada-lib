package nl.enjarai.cicada.api.conversation.conditions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import nl.enjarai.cicada.api.conversation.Conversation;
import nl.enjarai.cicada.api.util.IntRange;

import java.time.LocalDateTime;

public record DateCondition(IntRange year, IntRange month, IntRange weekDay,
                            IntRange day, IntRange hour, IntRange minute) implements LineCondition {
    public static final String TYPE = "cicada:date";
    public static final MapCodec<DateCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntRange.CODEC.optionalFieldOf("year", IntRange.ANY).forGetter(DateCondition::year),
            IntRange.CODEC.optionalFieldOf("month", IntRange.ANY).forGetter(DateCondition::month),
            IntRange.CODEC.optionalFieldOf("week_day", IntRange.ANY).forGetter(DateCondition::weekDay),
            IntRange.CODEC.optionalFieldOf("day", IntRange.ANY).forGetter(DateCondition::day),
            IntRange.CODEC.optionalFieldOf("hour", IntRange.ANY).forGetter(DateCondition::hour),
            IntRange.CODEC.optionalFieldOf("minute", IntRange.ANY).forGetter(DateCondition::minute)
    ).apply(instance, DateCondition::new));

    @Override
    public boolean test(Conversation conversation) {
        var now = LocalDateTime.now();
        return year.isInRange(now.getYear()) &&
                month.isInRange(now.getMonthValue()) &&
                weekDay.isInRange(now.getDayOfWeek().getValue()) &&
                day.isInRange(now.getDayOfMonth()) &&
                hour.isInRange(now.getHour()) &&
                minute.isInRange(now.getMinute());
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
