package cn.maodun.response;

import cn.maodun.config.FastMvcConfig;
import cn.maodun.exceptions.ExceptionWrapperAdvice;
import cn.maodun.model.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * 统一返回数据封装
 *
 * @author Diamond
 */
@ControllerAdvice
public class FastMvcResponseBodyAwareAdvice implements ResponseBodyAdvice<Object>, ApplicationListener<PayloadApplicationEvent<FastMvcConfig>> {

    private ObjectMapper objectMapper = new ObjectMapper();
    private Map<Method, Boolean> supportsCache = new WeakHashMap<>();

    @Autowired
    FastMvcConfig fastMvcConfig;

    @Autowired
    FastMvcResponseWrapper fastMvcResponseWrapper;

    @Override
    public void onApplicationEvent(PayloadApplicationEvent<FastMvcConfig> event) {
        this.supportsCache.clear();
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (supportsCache.containsKey(returnType.getMethod())) {
            return supportsCache.get(returnType.getMethod());
        }
        boolean isSupport = getIsSupport(returnType);
        supportsCache.put(returnType.getMethod(), isSupport);
        return isSupport;
    }

    private boolean getIsSupport(MethodParameter returnType) {
        Class<?> declaringClass = returnType.getMember().getDeclaringClass();

        ResponseWrapper responseWrapper = Optional.ofNullable(returnType.getMethod().getAnnotation(ResponseWrapper.class))
                .orElse(declaringClass.getAnnotation(ResponseWrapper.class));

        if (responseWrapper != null) {
            return responseWrapper.value();
        }
        if (fastMvcConfig.isEnableAutoWrapper()) {
            for (String basePackage : fastMvcConfig.getBasePackages()) {
                if (declaringClass.getPackage().getName().startsWith(basePackage)) {
                    return true;
                }
            }
            return ExceptionWrapperAdvice.class.isAssignableFrom(declaringClass);
        }
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        Object result;
        if (ErrorCode.class.isAssignableFrom(returnType.getParameterType())) {
            result = fastMvcResponseWrapper.errorWrapper((ErrorCode) body);
        } else {
            result = fastMvcResponseWrapper.resultWrapper(body);
        }
        if (returnType.getGenericParameterType().equals(String.class)) {
            try {
                response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                return objectMapper.writeValueAsString(result);
            } catch (JsonProcessingException ignore) {
            }
        }
        return result;
    }

}
