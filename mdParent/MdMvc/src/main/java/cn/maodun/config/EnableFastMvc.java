package cn.maodun.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启动快速MVC
 * @author zhouchao
 * @date: 2019-04-12
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@EnableConfigurationProperties(FastMvcConfig.class)
@Import({FastMvcConfiguration.class})
public @interface EnableFastMvc {
    /**
     * 处理返回值的包路径
     * @return
     */
    String [] basePackages() default {};

    /**
     * 是否自动包装返回值
     * @return
     */
    boolean enableAutoWrapper() default true;
}
