package cn.maodun.config;

import cn.maodun.ParamCheckInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author DELL
 * @date 2023/3/8
 */
//@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Autowired
    private ParamCheckInterceptor paramCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebMvcConfigurer.super.addInterceptors(registry);
        registry.addInterceptor(paramCheckInterceptor);
    }
}
