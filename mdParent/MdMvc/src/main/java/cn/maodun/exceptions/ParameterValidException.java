package cn.maodun.exceptions;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 参数校验异常
 *
 * @author shanhs
 */
public class ParameterValidException extends Exception {

    private final List<FieldErrorDTO> fieldErrors;

    public ParameterValidException(MethodArgumentNotValidException ex) {
        this.addSuppressed(ex);
        fieldErrors = ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    FieldErrorDTO fieldError = new FieldErrorDTO();
                    if (error instanceof org.springframework.validation.FieldError) {
                        org.springframework.validation.FieldError springError = (org.springframework.validation.FieldError) error;
                        fieldError.setName(springError.getField());
                    } else {
                        fieldError.setName(error.getObjectName());
                    }
                    fieldError.setMessage(error.getDefaultMessage());
                    return fieldError;
                }).collect(Collectors.toList());
    }

    public ParameterValidException(MissingServletRequestParameterException e) {
        this.addSuppressed(e);
        fieldErrors = Collections.singletonList(new FieldErrorDTO(e.getParameterName(), e.getMessage()));
    }

    public ParameterValidException(MethodArgumentTypeMismatchException ex) {
        this.addSuppressed(ex);
        fieldErrors = Collections.singletonList(new FieldErrorDTO(ex.getName(), ex.getMessage()));
    }

    public ParameterValidException(ConstraintViolationException violationException, MethodParameter[] methodParameters) {
        addSuppressed(violationException);
        this.fieldErrors = violationException.getConstraintViolations().stream().map(constraintViolation -> {
            PathImpl pathImpl = (PathImpl) constraintViolation.getPropertyPath();
            int paramIndex = pathImpl.getLeafNode().getParameterIndex();
            String paramName = methodParameters[paramIndex].getParameterName();
            FieldErrorDTO error = new FieldErrorDTO();
            error.setName(paramName);
            error.setMessage(constraintViolation.getMessage());
            return error;
        }).collect(Collectors.toList());
    }

    public ParameterValidException(BindException ex) {
        addSuppressed(ex);
        this.fieldErrors = bindExceptionToFieldError(ex);
    }

    public ParameterValidException(List<FieldErrorDTO> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public String buildErrorMessage() {
        return StringUtils.join(fieldErrors.stream().map(FieldErrorDTO::getMessage)
                .iterator(), "\n");
    }

    @Override
    public String getMessage() {
        StringBuilder sb = new StringBuilder();
        for (FieldErrorDTO fieldError : fieldErrors) {
            sb.append(fieldError.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    private List<FieldErrorDTO> bindExceptionToFieldError(BindException ex) {
        return ex.getFieldErrors().stream().map(f -> {
            FieldErrorDTO error = new FieldErrorDTO();
            error.setName(f.getObjectName() + "." + f.getField());
            error.setMessage(f.getDefaultMessage());
            return error;
        }).collect(Collectors.toList());
    }

    public List<FieldErrorDTO> getFieldErrors() {
        return fieldErrors;
    }
}
