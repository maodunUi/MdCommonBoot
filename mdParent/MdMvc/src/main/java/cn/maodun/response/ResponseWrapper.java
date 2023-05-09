package cn.maodun.response;

import java.lang.annotation.*;

/**
 * 返回值包装注解
 * @author Diamond
 * @see FastMvcResponseBodyAwareAdvice
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ResponseWrapper {
    /**
     * 是否将返回值通过二次包装后进行返回
     *
     * @return 默认true
     */
    boolean value() default true;
}
