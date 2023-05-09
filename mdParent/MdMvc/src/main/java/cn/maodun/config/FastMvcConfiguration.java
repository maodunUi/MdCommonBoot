package cn.maodun.config;


import cn.maodun.logs.HttpLogStorageService;
import cn.maodun.logs.RequestInputStreamWrapperFilter;
import cn.maodun.logs.RequestLogBuilder;
import cn.maodun.logs.RequestLogInterceptor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;

/**
 * MVC参数配置
 *
 * @author zhouchao
 * @date 2019-04-12
 */
@Configuration
@ComponentScan("cn.maodun.*")
public class FastMvcConfiguration implements WebMvcConfigurer {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Bean
    @Lazy
    public RequestLogBuilder requestLogBuilder() {
        return new RequestLogBuilder();
    }

    @Bean
    public RequestLogInterceptor requestLogInterceptor() {
        return new RequestLogInterceptor(requestLogBuilder());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLogInterceptor());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new TimestampConverter());
    }

    @Bean
    public FilterRegistrationBean requestInputStreamWrapperFilter() {
        FilterRegistrationBean<RequestInputStreamWrapperFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestInputStreamWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("requestInputWrapper");
        registration.setOrder(1);
        return registration;
    }



    @Bean
    public HttpLogStorageService httpLogStorageService() {
        return new HttpLogStorageService();
    }

    @Bean
    public RestTemplate getRestTemplate(HttpLogStorageService httpLogStorageService) {
        return getRestTemplate(DateUtils.MILLIS_PER_SECOND * 10,
                DateUtils.MILLIS_PER_SECOND * 60, httpLogStorageService);
    }

    /**
     * 生成RestTemplate
     *
     * @param connectTimeout        connectTimeout
     * @param readTimeout           readTimeout
     * @param httpLogStorageService log storage
     * @return restTemplate
     */
    public RestTemplate getRestTemplate(long connectTimeout, long readTimeout, HttpLogStorageService httpLogStorageService) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) connectTimeout);
        requestFactory.setReadTimeout((int) readTimeout);
        requestFactory.setOutputStreaming(true);
        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(requestFactory));
        restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
            long start = System.currentTimeMillis();
            ClientHttpResponse response = execution.execute(request, body);
            long end = System.currentTimeMillis();
            httpLogStorageService.log(request, body, response, start, end);
            return response;
        }));
        return restTemplate;
    }


}
