package cn.maodun.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志打印拦截器，用于打印所有HTTP请求的日志（debug级别）
 * @author zhouchao
 * @date: 2019-04-12
 */
@Slf4j
public class RequestLogInterceptor implements HandlerInterceptor {

    private final RequestLogBuilder requestLogBuilder;

    private final static String CAST_TIME_KEY = "_FAST_MAV_KEY_REQUEST_BEGIN_TIME";

    public RequestLogInterceptor(RequestLogBuilder requestLogBuilder) {
        this.requestLogBuilder = requestLogBuilder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(CAST_TIME_KEY, System.currentTimeMillis());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (log.isDebugEnabled()) {
            Long beginTime = (Long) request.getAttribute(CAST_TIME_KEY);
            long caseTime = 0;
            if (beginTime != null) {
                caseTime = System.currentTimeMillis() - beginTime;
            }
            String logInfo = requestLogBuilder.buildString(request, caseTime);
            log.debug(logInfo);
        }
    }
}
