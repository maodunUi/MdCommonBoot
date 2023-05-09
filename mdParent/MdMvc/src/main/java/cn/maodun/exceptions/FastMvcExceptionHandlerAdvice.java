package cn.maodun.exceptions;

import com.shanhs.fast.model.ErrorCode;
import com.shanhs.fast.mvc.logs.RequestLogBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;


/**
 * 统一异常处理，所有异常返回ErrorCode数据
 *
 * @author shanhs
 */
@RestControllerAdvice
@Slf4j
@Order
public class FastMvcExceptionHandlerAdvice implements ExceptionWrapperAdvice {
    @Autowired
    private RequestLogBuilder requestLogBuilder;

    @ExceptionHandler(AppException.class)
    public ErrorCode appException(AppException ex) {
        return doWrapper(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(ParameterValidException.class)
    public ErrorCode paramValidExceptionHandler(ParameterValidException ex) {
        log.info("param valid exception: ", ex);
        ErrorResultWrapper errorResultWrapper = doWrapperWithMessage(FastErrorCode.INVALID_PARAMS, ex.buildErrorMessage());
        errorResultWrapper.setErrorData(ex.getFieldErrors());
        return errorResultWrapper;
    }

    /**
     * 使用 @Validated 数据校验失败时
     * 当 model内参数类型转换错误时
     *
     * @param ex
     * @return 返回错误的结果包装类，并且在data内返回具体的错误字段和错误内容
     * @see BindException
     * @see org.springframework.validation.annotation.Validated
     */
    @ExceptionHandler(BindException.class)
    public ErrorCode bindExceptionHandler(BindException ex) {
        return paramValidExceptionHandler(new ParameterValidException(ex));
    }

    /**
     * 使用 @RequestParam 注解的参数没有值时，会抛出此异常
     *
     * @param e
     * @return 返回错误的结果包装类，并且在data内返回该注解参数名称
     * @see org.springframework.web.bind.annotation.RequestParam
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ErrorCode missingServletRequestParameterException(MissingServletRequestParameterException e) {
        return paramValidExceptionHandler(new ParameterValidException(e));
    }

    /**
     * 当 Controller方法参数使用 @RequestBody注解，并且添加了校验注解 @Valid 或 @Validated，数据校验失败时会抛出此异常
     *
     * @return 返回错误的结果包装类，并且在data内返回具体的错误字段和错误内容
     * @see javax.validation.Valid
     * @see org.springframework.validation.annotation.Validated
     * @see org.springframework.web.bind.annotation.RequestBody
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorCode methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex) {
        return paramValidExceptionHandler(new ParameterValidException(ex));
    }

    /**
     * 当在 Controller上使用了 @Validated 注解，并且在参数上添加了校验类注解，当参数校验失败时，抛出异常
     *
     * @param ex            异常信息
     * @param handlerMethod 当前执行的参数
     * @return 返回错误的结果包装类，并且在data内返回具体的错误字段和错误内容
     * @see javax.validation.constraints
     * @see org.springframework.validation.annotation.Validated
     * @see org.springframework.validation.beanvalidation.MethodValidationPostProcessor
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ErrorCode constraintViolationExceptionHandler(ConstraintViolationException ex, HandlerMethod handlerMethod) {
        return paramValidExceptionHandler(new ParameterValidException(ex, handlerMethod.getMethodParameters()));
    }

    /**
     * 当指定方法不支持该类型的请求方式时，抛出此异常
     * 例如 /a 只支持 POST 请求, 前端却使用 GET 请求进行访问
     *
     * @param servletRequest
     * @param ex
     * @return 请求方式错误异常包装类，会返回请求的方式+路径信息
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorCode httpRequestMethodNotSupportedExceptionHandler(HttpServletRequest servletRequest, HttpRequestMethodNotSupportedException ex) {
        log.info("may request method error: ", ex);
        return doWrapperWithMessage(FastErrorCode.REQUEST_METHOD_NOT_SUPPORT,
                "[" + servletRequest.getMethod() + "]" + servletRequest.getRequestURL());
    }

    /**
     * 参数类型转换失败时抛出异常，并且会打印出完整请求体日志（info）级别
     *
     * @param ex
     * @param request
     * @return 返回错误的结果包装类，并且在data内返回具体的错误字段和错误内容
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorCode methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.info(requestLogBuilder.buildString(request, 0), ex);
        return paramValidExceptionHandler(new ParameterValidException(ex));
    }

    /**
     * 所有未捕获的异常处理
     *
     * @param ex       异常
     * @param response 响应
     * @param request  请求
     * @return 异常编码
     */
    @ExceptionHandler(Exception.class)
    public ErrorCode exception(Exception ex, HttpServletResponse response, HttpServletRequest request) {
        response.setStatus(500);
        log.error("{}", requestLogBuilder.buildString(request, 0), ex);
        return doWrapper(FastErrorCode.UNKNOWN_EXCEPTION);
    }

}
