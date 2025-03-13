package com.tencent.wxcloudrun.common.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.filter.SecurityFilter;
import com.tencent.wxcloudrun.utils.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

/**
 * <p> 全局异常处理器 </p>
 *
 * @author Houcloud
 */
@Slf4j
@RestControllerAdvice
public class BusinessExceptionHandler {

    @Value("${spring.profiles.active:none}")
    private String env;

    @Value("${spring.application.name:Application'}")
    private String applicationName;


    private final SecurityFilter securityFilter;

    @Autowired
    public BusinessExceptionHandler(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }


    /**
     * 爬虫等防范
     *
     * @param exception
     * @param request
     * @return
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleException(HttpMediaTypeNotAcceptableException exception, HttpServletRequest request) {
        String requestIp = IpUtil.getRealIp(request); // 获取请求的 IP
        // todo 增加请求计数
        log.warn("不规范非法的请求\n{} \n{}", exception.getHeaders(), exception.getMessage());
        return Result.fail(exception.getMessage());
    }

    /**
     * 自定义异常处理
     *
     * @return R
     */
    @ExceptionHandler({BusinessException.class})
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleException(BusinessException exception) {
        log.warn("业务异常\n{}", exception.getMessage());
        return Result.fail(exception.getCode(), exception.getMessage());
    }

    /**
     * 找不到资源
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleException(NoResourceFoundException e) {
        return Result.notfound();
    }


    /**
     * 处理请求参数异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleException(MissingServletRequestParameterException exception) {
        String name = exception.getParameterName();
        log.warn("请求参数{}异常", name, exception);
        return Result.fail("请求缺少" + name + "参数");
    }


    /**
     * 方法参数类型不匹配异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleException(MethodArgumentTypeMismatchException exception) {
        String name = exception.getName();
        log.warn("请求参数{}异常", name, exception);
        return Result.fail("参数" + name + "类型校验错误");
    }

    /**
     * HTTP消息读取异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleException(HttpMessageNotReadableException exception) {
        log.warn("参数绑定异常\n{}", exception.getHttpInputMessage());
        return Result.fail("参数校验错误", exception.getMessage());
    }

    /**
     * 参数绑定异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleException(BindException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        log.warn("参数异常\n{}", fieldErrors.get(0).getDefaultMessage());
        return Result.fail("参数校验异常", fieldErrors.get(0).getDefaultMessage());
    }

    /**
     * validation Exception (以form-data形式传参)
     * 格式错误异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({InvalidFormatException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<String> handleException(InvalidFormatException exception) {
        Object value = exception.getValue();
        log.warn("参数验证异常\n{}", value);
        return Result.fail("参数验证异常", value.toString() + exception.getMessage());
    }

    /**
     * 请求方法不支持异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result<Void> handleException(HttpServletRequest request, HttpRequestMethodNotSupportedException exception) {
        String method = exception.getMethod();
        log.warn("请求 {} 不支持{}方法", request.getRequestURL(), method);
        return Result.fail(method + "方法请求无效");
    }


    /**
     * 处理运行异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(HttpServletRequest request, RuntimeException exception) {
        log.warn("异常类:{}", exception.getClass());
        log.warn("系统繁忙", exception);
        return Result.fail(BusinessStatus.SERVICE_BUSY.getCode(), BusinessStatus.SERVICE_BUSY.getMessage(), exception.getMessage());
    }


    /**
     * 处理未知异常
     *
     * @param exception
     * @return R
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<String> handleException(HttpServletRequest request, Exception exception) {
        log.warn("异常类:{}", exception.getClass());
        log.warn("未知异常", exception);
        return Result.fail(BusinessStatus.UNKNOWN.getCode(), BusinessStatus.UNKNOWN.getMessage(), exception.getMessage());
    }



}
