package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import com.shanhs.fast.redis.RedisLockGetTimeoutException;
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
@ConditionalOnClass(RedisLockGetTimeoutException.class)
@Order(1)
public class FastRedisTimeoutExceptionHandlerAdvice implements ExceptionWrapperAdvice{
    @ExceptionHandler(RedisLockGetTimeoutException.class)
    public ErrorCode redisLockGetTimeoutException(RedisLockGetTimeoutException ex) {
        log.info("RedisGetLockTimeout: {}", ex.getMessage(), ex);
        return doWrapper(FastErrorCode.TOO_MANY_REQUEST);
    }

}
