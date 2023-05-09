package cn.maodun.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Diamond
 */
@ConfigurationProperties("fast.start.mvc")
@Getter
@Setter
public class FastMvcConfig implements ApplicationContextAware, InitializingBean {
    private ApplicationContext applicationContext;
    /**
     * 当前应用的基础包路径，默认是启动类所在的包，也可以在 @EnableFastMvc注解内显示指定
     */
    private String[] basePackages;

    /**
     * 是否启用自动包装，(返回值，参数错误自动包装)
     */
    private boolean enableAutoWrapper = true;

    public FastMvcConfig(String[] basePackages) {
        this.basePackages = basePackages;
    }

    @Override
    public void afterPropertiesSet() {
        Collection<Object> springBootApplicationBeans = getApplicationContext().getBeansWithAnnotation(EnableFastMvc.class).values();
        Set<String> basePackageSet = new LinkedHashSet<>();
        for (Object obj : springBootApplicationBeans) {
            EnableFastMvc annotation = AnnotationUtils.findAnnotation(obj.getClass(), EnableFastMvc.class);
            if (annotation.basePackages().length > 0) {
                basePackageSet.addAll(Arrays.asList(annotation.basePackages()));
            }
            if (!annotation.enableAutoWrapper()) {
                this.enableAutoWrapper = false;
            }
        }
        if (ArrayUtils.isEmpty(this.basePackages)) {
            if (basePackageSet.size() > 0) {
                this.basePackages = basePackageSet.toArray(new String[0]);
            } else {
                this.basePackages = applicationContext.getBeansWithAnnotation(SpringBootApplication.class).values()
                        .stream().map(bean -> bean.getClass().getPackage().getName()).distinct().toArray(String[]::new);
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setBasePackages(String[] basePackages) {
        this.basePackages = basePackages;
        getApplicationContext().publishEvent(this);
    }

    public void setEnableAutoWrapper(boolean enableAutoWrapper) {
        this.enableAutoWrapper = enableAutoWrapper;
        getApplicationContext().publishEvent(this);
    }
}

