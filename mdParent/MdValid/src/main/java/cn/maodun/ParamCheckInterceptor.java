package cn.maodun;

import cn.maodun.annotation.ParamCheck;
import cn.maodun.annotation.RuleStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * @author DELL
 * @date 2022/7/19
 */
@Service
public class ParamCheckInterceptor implements HandlerInterceptor, ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse httpServletResponse, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod methodHandler = (HandlerMethod) handler;
            Parameter[] parameters = methodHandler.getMethod().getParameters();
            for (Parameter parameter : parameters) {
                Class<?> type = parameter.getType();
                Field[] fields = type.getFields();
                for (Field field : fields) {
                    ParamCheck annotation = field.getAnnotation(ParamCheck.class);
                    if (Objects.nonNull(annotation)) {
                        String name = field.getName();
                        String value = request.getParameter(name);
                        Map<String, RuleStrategy> beansOfType = applicationContext.getBeansOfType(RuleStrategy.class);
                        Collection<RuleStrategy> ruleStrategys = beansOfType.values();
                        ruleStrategys.stream().forEach(r -> {
                            r.rule(value, annotation);
                        });
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ParamCheckInterceptor.applicationContext = applicationContext;
    }
}
