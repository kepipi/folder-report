package com.sztus.lib.back.end.basic.handler;


import com.sztus.lib.back.end.basic.exception.BusinessException;
import com.sztus.lib.back.end.basic.exception.ParametersException;
import com.sztus.lib.back.end.basic.type.Result;
import com.sztus.lib.back.end.basic.type.constant.CommonConst;
import com.sztus.lib.back.end.basic.type.constant.GlobalConst;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MAX
 * @date 2022.09.09
 */
@Slf4j
@RestControllerAdvice
public class BaseExceptionHandler {

    /**
     * 处理基类异常
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> exceptionHandler(HttpServletRequest request, Exception exception) {
        return Result.fail(generateExceptionInfo(request, exception));
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> exceptionHandler(HttpServletRequest request, HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException Parameter is {} ", request.getParameterMap());
        log.error("HttpMessageNotReadableException Exception is ", exception);
        return Result.fail(exception.getMessage());
    }

    @ExceptionHandler(value = ClientAbortException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> exceptionHandler(HttpServletRequest request, ClientAbortException exception) {
        log.error("ClientAbortException Parameter is {} ", request.getParameterMap());
        log.error("ClientAbortException Exception is ", exception);
        return Result.fail(exception.getMessage());
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Object> exceptionHandler(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        log.error("HttpRequestMethodNotSupportedException Parameter is {} ", request.getParameterMap());
        log.error("HttpRequestMethodNotSupportedException Exception is ", exception);
        return Result.fail(exception.getMessage());
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> exceptionHandler(HttpServletRequest request, BusinessException exception) {
        return Result.fail(exception);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(value = ParametersException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Map<String, Object>> exceptionHandler(HttpServletRequest request, ParametersException exception) {
        generateExceptionInfo(request, exception);
        HashMap<String, Object> map = new HashMap<>(2);
        map.put(CommonConst.REASON, exception.getMessage());
        return Result.fail("Parameter Verification Failed", map);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> exceptionHandler(HttpServletRequest request, NullPointerException exception) {
        return Result.fail(generateExceptionInfo(request, exception));
    }

    /**
     * 处理参数验证异常
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Result<Object> exceptionHandler(HttpServletRequest request, BindException exception) {
        String exceptionMessage = GlobalConst.STR_EMPTY;

        BindingResult bindingResult = exception.getBindingResult();
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : allErrors) {
                FieldError fieldError = (FieldError) error;
                String defaultMessage = fieldError.getDefaultMessage();
                String field = fieldError.getField();
                errorMessage.append(String.format("%s %s; ", field, defaultMessage));
            }
            exceptionMessage = errorMessage.toString();
        }

        return Result.fail(exceptionMessage);
    }


    /**
     * 获取异常的堆栈信息
     *
     * @param throwable 获取的异常父类
     * @return 堆栈信息
     */
    public static String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            throwable.printStackTrace(pw);
            return sw.toString();
        }
    }


    private String generateExceptionInfo(HttpServletRequest request, Exception exception) {
        String exceptionName = exception.getClass().getSimpleName();
        String stackTrace = getStackTrace(exception);

        log.error(" {} was catch : {}", exceptionName, stackTrace);
        String path = request.getRequestURI();
        String requestMethod = request.getMethod();

//        DingTalkUtil.sendProgramExceptionMessage(path, exceptionName, requestMethod);

        return HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase();
    }

}