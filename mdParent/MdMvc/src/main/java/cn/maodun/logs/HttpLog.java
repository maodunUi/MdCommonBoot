package cn.maodun.logs;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Diamond
 */
@Data
public class HttpLog implements Serializable {
    private String requestId;
    private String requestUri;
    private String requestHost;
    private String requestPath;
    private String requestQuery;
    private String requestBody;
    private Integer responseStatusCode;
    private String responseBody;
    private Date startTime;
    private Date endTime;
    private Integer duration;
    private String requestHeaders;
    private String responseHeaders;
    private String appName;
}
