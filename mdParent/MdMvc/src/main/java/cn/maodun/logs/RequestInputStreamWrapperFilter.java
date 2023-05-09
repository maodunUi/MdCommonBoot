package cn.maodun.logs;

import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 将 post 请求中的数据流部分包装为可重复度的 java.io.ByteArrayInputStream，用于日志输出
 * @author shanhs
 */
public class RequestInputStreamWrapperFilter extends OncePerRequestFilter {
    public class RepeatReadHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final byte[] body;

        RepeatReadHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            try (ServletInputStream inputStream = request.getInputStream()) {
                List<Byte> byteList = new ArrayList<>();
                int data;
                while ((data = inputStream.read()) > -1) {
                    byteList.add((byte) data);
                }
                body = new byte[byteList.size()];
                for (int i = 0; i < byteList.size(); i++) {
                    body[i] = byteList.get(i);
                }
            }
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() {
                    return inputStream.read();
                }
            };
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (logger.isDebugEnabled()) {
            if (MediaType.APPLICATION_JSON_VALUE.equalsIgnoreCase(httpServletRequest.getContentType())) {
                RepeatReadHttpServletRequestWrapper requestWrapper = new RepeatReadHttpServletRequestWrapper(httpServletRequest);
                doFilter(requestWrapper, httpServletResponse, filterChain);
                return;
            }
        }
        doFilter(httpServletRequest, httpServletResponse, filterChain);
    }
}
