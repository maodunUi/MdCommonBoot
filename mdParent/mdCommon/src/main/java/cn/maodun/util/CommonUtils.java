package cn.maodun.util;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author DELL
 * @date 2023/5/5
 */
public class CommonUtils {

    /**
     * 集合映射后求和，支持identity作为初始求和值
     * @param collection
     * @param mapper
     * @param identity
     * @param <T>
     * @return
     */
    public static <T> BigDecimal sum(Collection<T> collection, Function<T, BigDecimal> mapper, BigDecimal identity){
        return collection.stream().map(mapper).reduce(identity, BigDecimal::add);
    }

    /**
     * 集合映射后求和，初始求和值=0
     * @param collection
     * @param mapper
     * @param <T>
     * @return
     */
    public static <T> BigDecimal sum(Collection<T> collection, Function<T, BigDecimal> mapper){
        return sum(collection, mapper, BigDecimal.ZERO);
    }
}
