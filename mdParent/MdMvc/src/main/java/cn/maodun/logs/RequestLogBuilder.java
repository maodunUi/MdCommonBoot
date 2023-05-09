package cn.maodun.logs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;

/**
 * 打印请求日志
 *
 * @author shanhs
 */
@Slf4j
public class RequestLogBuilder {

    @Autowired(required = false)
    UserPrincipalProvider userPrincipalProvider;

    /**
     * 构建日志字符串
     *
     * @param request  请求
     * @param caseTime 请求处理时间
     * @return 拼接CHROME风格HTTP请求日志
     */
    public String buildString(HttpServletRequest request, long caseTime) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        sb.append("\nFrom: ").append(getRemoteAddress(request));
        Optional.ofNullable(userPrincipalProvider).map(UserPrincipalProvider::getPrincipal)
                .ifPresent(principal -> sb.append("\nUserPrincipal: ").append(principal));

        sb.append("\nRequest Times: ").append(caseTime).append(" ms");
        sb.append("\nRequest URL: ").append(request.getRequestURL());
        sb.append("\nRequest METHODS: ").append(request.getMethod());
        sb.append("\nRequest Headers: \n");
        while (headerNames.hasMoreElements()) {
            String header = headerNames.nextElement();
            sb.append("    ").append(header).append(": ").append(request.getHeader(header));
            sb.append("\n");
        }
        Map<String, String[]> paramsMap = request.getParameterMap();
        sb.append("Form Data\n");
        paramsMap.forEach((k, v) -> {
            sb.append("    ");
            sb.append(k).append(": ");
            sb.append(Optional.ofNullable(v).map(Arrays::toString).orElse(""));
            sb.append("\n");
        });
        if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(request.getContentType())) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(request.getInputStream(), Charset.forName("UTF-8")))) {
                String line;
                sb.append("    ");
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } catch (IOException e) {
                log.warn("log to print json value error: ", e);
                sb.append("WARNING: log json value io exception: ")
                        .append(e.getMessage());
            }
        }
        return sb.toString();
    }

    private static String getRemoteAddress(HttpServletRequest request) {
        String remote = request.getHeader("x-forwarded-for");
        if (remote == null) {
            remote = request.getRemoteAddr();
        }
        return remote;
    }
}
