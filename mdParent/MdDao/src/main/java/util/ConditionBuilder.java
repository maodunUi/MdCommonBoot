package util;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * JOOQ Condition Builder
 * @author Diamond
 */
public interface ConditionBuilder {
    /**
     * 构建AND类型的语句
     * @param optionalCollection 约束集合
     * @return 约束条件
     */
    static Condition optionsToAnd(Collection<Optional<Condition>> optionalCollection) {
        return optionalCollection.stream().filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Condition::and)
                .orElse(DSL.noCondition());
    }

    /**
     * 构建OR类型的语句
     * @param optionCondition 约束集合
     * @return 约束对象
     */
    static Condition optionsToOr(Optional<Condition> ... optionCondition) {
        return optionsToOr(Arrays.asList(optionCondition));
    }

    /**
     * 构建OR类型的语句
     * @param optionalCollection 约束集合
     * @return 约束对象
     */
    static Condition optionsToOr(Collection<Optional<Condition>> optionalCollection) {
        return optionalCollection.stream().filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Condition::or)
                .orElse(DSL.noCondition());
    }

    /**
     * 构建And类型约束语句
     * @param optionals 约束集合
     * @return 约束对象
     */
    static Condition optionsToAnd(Optional<Condition>... optionals) {
        return optionsToAnd(Arrays.asList(optionals));
    }

    /**
     * 必须是 参数，Condition， 参数 Condition
     *
     * @return 约束对象
     */
    static <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5,
            T6 p6, Function<T6, Condition> f6,
            T7 p7, Function<T7, Condition> f7,
            T8 p8, Function<T8, Condition> f8,
            T9 p9, Function<T9, Condition> f9,
            T10 p10, Function<T10, Condition> f10
    ) {
        return Stream.of(
                Optional.ofNullable(p1).map(a -> f1 == null ? null: f1.apply(a)),
                Optional.ofNullable(p2).map(a -> f2 == null ? null : f2.apply(a)),
                Optional.ofNullable(p3).map(a -> f3 == null ? null : f3.apply(a)),
                Optional.ofNullable(p4).map(a -> f4 == null ? null : f4.apply(a)),
                Optional.ofNullable(p5).map(a -> f5 == null ? null : f5.apply(a)),
                Optional.ofNullable(p6).map(a -> f6 == null ? null : f6.apply(a)),
                Optional.ofNullable(p7).map(a -> f7 == null ? null : f7.apply(a)),
                Optional.ofNullable(p8).map(a -> f8 == null ? null : f8.apply(a)),
                Optional.ofNullable(p9).map(a -> f9 == null ? null : f9.apply(a)),
                Optional.ofNullable(p10).map(a -> f10 == null ? null : f10.apply(a)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .reduce(Condition::and).orElse(DSL.noCondition());
    }


    static <T1, T2, T3, T4, T5, T6, T7, T8, T9> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5,
            T6 p6, Function<T6, Condition> f6,
            T7 p7, Function<T7, Condition> f7,
            T8 p8, Function<T8, Condition> f8,
            T9 p9, Function<T9, Condition> f9) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, p5, f5, p6, f6, p7, f7, p8, f8, p9, f9, null, null);
    }

    static <T1, T2, T3, T4, T5, T6, T7, T8> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5,
            T6 p6, Function<T6, Condition> f6,
            T7 p7, Function<T7, Condition> f7,
            T8 p8, Function<T8, Condition> f8) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, p5, f5, p6, f6, p7, f7, p8, f8, null, null);
    }

    static <T1, T2, T3, T4, T5, T6, T7> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5,
            T6 p6, Function<T6, Condition> f6,
            T7 p7, Function<T7, Condition> f7) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, p5, f5, p6, f6, p7, f7, null, null);
    }

    static <T1, T2, T3, T4, T5, T6> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5,
            T6 p6, Function<T6, Condition> f6) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, p5, f5, p6, f6, null, null);
    }

    static <T1, T2, T3, T4, T5> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4,
            T5 p5, Function<T5, Condition> f5) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, p5, f5, null, null);
    }

    static <T1, T2, T3, T4> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3,
            T4 p4, Function<T4, Condition> f4) {
        return buildCondition(p1, f1, p2, f2, p3, f3, p4, f4, null, null);
    }

    static <T1, T2, T3> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2,
            T3 p3, Function<T3, Condition> f3) {
        return buildCondition(p1, f1, p2, f2, p3, f3, null, null);
    }

    static <T1, T2> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1,
            T2 p2, Function<T2, Condition> f2) {
        return buildCondition(p1, f1, p2, f2, null, null);
    }

    static <T1> Condition buildCondition(
            T1 p1, Function<T1, Condition> f1) {
        return buildCondition(p1, f1, null, null);
    }

    /**
     * 创建一个时间范围condition
     * @param field 时间字段
     * @return 可以放在buildCondition的函数
     */
    static Function<Timestamp[], Condition> createTimeRangeCondition(Field<Timestamp> field) {
        return createTimeRangeCondition(field, false);
    }

    /**
     * 创建以天单位的时间范围筛选条件
     * @param field 范围条件字段
     * @return 可以放在buildCondition的函数，自动处理结束时间为该天的最大时间
     */
    static Function<Timestamp[], Condition> createDayRangeCondition(Field<Timestamp> field) {
        return createTimeRangeCondition(field, true);
    }

    /**
     * 创建一个时间范围condition
     * @param field 时间字段
     * @param withMaxLocalTime 是否将结束时间设为当天最大时间
     * @return 可以放在buildCondition的函数
     */
    static Function<Timestamp[], Condition> createTimeRangeCondition(Field<Timestamp> field, boolean withMaxLocalTime) {
        return timeRangeValue -> {
            if (timeRangeValue == null) {
                return DSL.noCondition();
            } else if (timeRangeValue.length  == 1) {
                return field.ge(timeRangeValue[0]);
            } else if (timeRangeValue.length == 2) {
                Timestamp endTime = timeRangeValue[1];
                if (withMaxLocalTime && endTime != null) {
                    endTime = Timestamp.valueOf(endTime.toLocalDateTime().with(LocalTime.MAX));
                }
                return field.between(timeRangeValue[0], endTime);
            }
            return DSL.noCondition();
        };
    }
}
