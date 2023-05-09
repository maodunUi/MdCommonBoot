package cn.maodun.model;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 枚举映射类
 * @author Diamond
 */
public interface EnumMapping {
    /**
     * 枚举类型，枚举名称
     * @return 枚举名称
     * @see Enum#name()
     */
    String name();
    /**
     * 获取映射key
     * @return 返回映射key
     */
    default String getKey() {
        return this.name();
    }

    /**
     * 获取映射文本
     * @return 返回映射文本
     */
    String getText();

    Map<Class<?>, Map<String, String>> ENUM_CACHE = new WeakHashMap<>(2);

    /**
     * 将枚举类转换为Map
     * @param type 枚举类
     * @return map
     */
    static Map<String, String> toMap(Class<? extends EnumMapping> type) {
        Map<String, String> map = ENUM_CACHE.get(type);
        try {
            if (map == null) {
                Method methodValues = type.getMethod("values");
                EnumMapping[] mappings = (EnumMapping[]) methodValues.invoke(null);
                Map<String, String> keyValueMap = new LinkedHashMap<>();
                Arrays.stream(mappings).forEach(m -> keyValueMap.put(m.getKey(), m.getText()));
                map = keyValueMap;
            }
        } catch (Exception e) {
            map = Collections.emptyMap();
        }
        ENUM_CACHE.putIfAbsent(type, map);
        return map;
    }
}
