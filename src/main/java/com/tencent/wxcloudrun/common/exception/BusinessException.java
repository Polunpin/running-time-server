package com.tencent.wxcloudrun.common.exception;

import lombok.Getter;

import java.io.Serial;

/**
 * <p> 自定义异常 </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Getter
public class BusinessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3034121940056795549L;

    private Integer code;

    public BusinessException() {

    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public BusinessException(BusinessStatus status) {
        super(status.getMessage());
        this.code = status.getCode();
    }

    public static BusinessException exception(String message) {
        return new BusinessException(message, BusinessStatus.ERROR.getCode());
    }

    public static BusinessException exception(int code, String message) {
        return new BusinessException(message, code);
    }

    public static BusinessException exception(BusinessStatus businessStatus) {
        return new BusinessException(businessStatus.getMessage(), businessStatus.getCode());
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
