package cn.maodun;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DELL
 * @date 2023/3/29
 */

/**
 *
 * @SpringBootConfiguration
 *  代表是一个配置类
 * @EnableAutoConfiguration
 *          AutoConfigurationImportSelector
 *              org.springframework.core.io.support.SpringFactoriesLoader#loadSpringFactories(java.lang.ClassLoader)
 *                  从META-INF/spring.factories位置来加载文件
 *                  找到spring-boot-autoconfigure中的spring.factories文件。我们这里整合es我们主要看es的配置。可以找到ElasticsearchRestClientAutoConfiguration配置类
 *
 * @ComponentScan
 *
 *
 */
@SpringBootApplication
public class SpingBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpingBootApplication.class, args);
    }
}
