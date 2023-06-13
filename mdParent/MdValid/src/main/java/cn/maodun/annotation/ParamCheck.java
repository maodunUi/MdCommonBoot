package cn.maodun.annotation;


import java.lang.annotation.*;

/**
 * @author DELL
 * @date 2022/7/19
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {
    String rule() default "";

    String msg() default "";

    String patternRegex() default "";
}
