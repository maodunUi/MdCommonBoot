package cn.maodun.logs;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.MimeType;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * 统一储存http请求日志服务，请求log-api进行
 *
 * @author Diamond
 */
@Slf4j
public class HttpLogStorageService {

//    @Autowired(required = false)
//    DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    String appName;

    private final RestTemplate restTemplate = new RestTemplate();

    public void log(HttpRequest request, byte[] body, ClientHttpResponse response, long start, long end) {
        try {
            HttpLog logRequestCommon = new HttpLog();
            logRequestCommon.setRequestId(UUID.randomUUID().toString().replace("-", ""));
            logRequestCommon.setStartTime(new Date(start));
            logRequestCommon.setEndTime(new Date(end));
            logRequestCommon.setDuration((int) (end - start));
            logRequestCommon.setRequestUri(StringUtils.substring(request.getURI().toString(), 0, 2000));
            logRequestCommon.setRequestHost(StringUtils.substring(request.getURI().getHost(), 0, 200));
            logRequestCommon.setRequestPath(StringUtils.substring(request.getURI().getPath(), 0, 200));
            logRequestCommon.setRequestQuery(StringUtils.substring(request.getURI().getQuery(), 0, 200));
            logRequestCommon.setRequestBody(StringUtils.substring(new String(body), 0, 2000));
            logRequestCommon.setResponseStatusCode(response.getRawStatusCode());
            logRequestCommon.setRequestHeaders(StringUtils.substring(request.getHeaders().toString(), 0, 800));
            logRequestCommon.setResponseHeaders(StringUtils.substring(response.getHeaders().toString(), 0, 800));
            logRequestCommon.setAppName(appName);
            if (response.getStatusCode().is2xxSuccessful()) {
                try {
                    Charset charset = Optional.of(response.getHeaders())
                            .map(HttpHeaders::getContentType)
                            .map(MimeType::getCharset)
                            .orElse(null);
                    if (charset == null || !charset.isRegistered()) {
                        charset = StandardCharsets.UTF_8;
                    }
//                    logRequestCommon.setResponseBody(StringUtils.substring(IOUtils.toString(response.getBody(), charset), 0, 2000));
                } catch (Exception ex) {
                    log.error("设置通用请求日志responseBody失败", ex);
                }
            }

//            if (discoveryClient != null) {
//                CompletableFuture.runAsync(() -> syncToLogApi(logRequestCommon));
//            }
            log.info("[CommonLog] request finish requestId: {}", logRequestCommon.getRequestId());
        } catch (Exception ex) {
            log.error("插入通用请求日志失败", ex);
        }
    }

//    private void syncToLogApi(HttpLog logRequestCommon) {
//        List<ServiceInstance> instances = discoveryClient.getInstances("log-api");
//        for (ServiceInstance instance : instances) {
//            String logRequestPath = "/log/http-log";
//            String requestUrl = instance.getUri().toString() + logRequestPath;
//            try {
//                ResponseEntity<String> response = restTemplate.postForEntity(requestUrl, logRequestCommon, String.class);
//                if (log.isDebugEnabled()) {
//                    log.debug("sync to log-api success: {}", response);
//                }
//                return;
//            } catch (Exception e) {
//                log.warn("sync to log-api url: {} error: {}", requestUrl, logRequestCommon, e);
//            }
//        }
//    }
}
