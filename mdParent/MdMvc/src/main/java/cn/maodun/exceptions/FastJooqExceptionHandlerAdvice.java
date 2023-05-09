package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.jooq.exception.DataAccessException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Diamond
 */
@RestControllerAdvice
@Slf4j
@ConditionalOnClass(DataAccessException.class)
@Order(1)
public class FastJooqExceptionHandlerAdvice implements ExceptionWrapperAdvice {
    @ExceptionHandler(DataAccessException.class)
    public ErrorCode jooqInvalidResultException(DataAccessException error) {
        log.error("JooqAccessException: ", error);
        return doWrapper(FastErrorCode.INVALID_RESULT);
    }
}
