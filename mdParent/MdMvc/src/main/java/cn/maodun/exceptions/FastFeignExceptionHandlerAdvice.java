package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Diamond
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnClass(FeignException.class)
@Order(1)
public class FastFeignExceptionHandlerAdvice implements ExceptionWrapperAdvice {
    @ExceptionHandler(AppException.class)
    public ErrorCode appException(AppException ex) {
        return doWrapper(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(FeignException.class)
    public ErrorCode feignException(FeignException error) {
        Throwable cause = error.getCause();
        if (cause instanceof AppException) {
            return appException((AppException) cause);
        }
        if (error instanceof AppException) {
            return appException((AppException) error);
        }
        log.error("FeignCallError: ", error);
        return doWrapperWithMessage(FastErrorCode.THIRD_CALL_FAILED, error.getMessage());
    }

}
