package cn.maodun.response;

import cn.maodun.model.ErrorCode;
import cn.maodun.model.FastResult;

import java.util.Objects;

/**
 * 默认响应包装实现类
 *
 * @author Diamond
 */
public class DefaultResponseWrapper implements FastMvcResponseWrapper {
    @Override
    public Object resultWrapper(Object data) {
        if (data == null) {
            return new FastResult<>();
        }
        if (Objects.equals(FastResult.class, data.getClass())) {
            return data;
        }
        return new FastResult<>(data);
    }

    @Override
    public Object errorCodeWrapper(int code, String message) {
        return new FastResult<>(new ErrorCode() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public int getCode() {
                return code;
            }
        });
    }
}

