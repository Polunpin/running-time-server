package com.tencent.wxcloudrun.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;


/**
 * 统一响应对象
 *
 * @author Houcloud
 */
@Data
@Slf4j
@Schema(description = "响应参数")
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 3468352004150968551L;

    /**
     * 状态码
     */
    @Schema(description = "状态码")
    private Integer code;

    /**
     * 消息
     */
    @Schema(description = "提示消息")
    private String message;

    @Schema(description = "是否成功")
    private Boolean success;

    /**
     * 返回对象
     */
    @Schema(description = "接口核心参数")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public Result() {
        super();
    }

    public Result(Integer code) {
        super();
        this.code = code;
    }

    public Result(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Boolean success, T result) {
        this.code = code;
        this.message = message;
        this.success = success;
        this.result = result;
    }

    public Result(Integer code, String message, Boolean success) {
        this.code = code;
        this.message = message;
        this.success = success;
    }

    public Result(Integer code, Throwable throwable) {
        super();
        this.code = code;
        this.message = throwable.getMessage();
    }

    public Result(Integer code, String message, T result) {
        super();
        this.code = code;
        this.message = message;
        this.result = result;
    }


    public static <T> Result<T> success() {
        return new Result<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), true);
    }

    public static Result<Void> success(ResultStatus status) {
        return new Result<>(status.getCode(), status.getMessage(), true);
    }

    public static <T> Result<T> success(String message) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), message, true);
    }

    public static <T> Result<T> success(T result) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), ResultStatus.SUCCESS.getMessage(), true, result);
    }

    public static <T> Result<T> success(String message, T result) {
        return new Result<>(ResultStatus.SUCCESS.getCode(), message, true, result);
    }

    public static <T> Result<T> fail() {
        log.warn("业务异常响应: FAIL");
        return new Result<>(ResultStatus.FAIL.getCode(), ResultStatus.FAIL.getMessage(), false);
    }

    public static <T> Result<T> fail(ResultStatus status) {
        log.warn("业务异常响应: [{}] {}", status.getCode(), status.getMessage());
        return new Result<>(status.getCode(), status.getMessage(), false);
    }

    public static <T> Result<T> fail(String message) {
        log.warn("业务异常响应: {}", message);
        return new Result<>(ResultStatus.FAIL.getCode(), message, false);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        log.warn("业务异常响应: [{}] {}", code, message);
        return new Result<>(code, message, false);
    }

    public static <T> Result<T> fail(String message, T result) {
        log.warn("业务异常响应: {} - {}", message, result);
        return new Result<>(ResultStatus.FAIL.getCode(), message, false, result);
    }

    public static <T> Result<T> fail(Integer code, String message, T result) {
        log.warn("业务异常响应: [{}] {} - {}", code, message, result);
        return new Result<>(code, message, false, result);
    }

    public static <T> Result<T> noPrivilege() {
        log.warn("业务异常响应: 403 NO PRIVILEGE");
        return new Result<>(ResultStatus.ACCESS_FAIL.getCode(), ResultStatus.ACCESS_FAIL.getMessage(), false);
    }

    public static <T> Result<T> noPrivilege(String message) {
        log.warn("业务异常响应: 403 NO PRIVILEGE {}", message);
        return new Result<>(ResultStatus.ACCESS_FAIL.getCode(), message, false);
    }

    public static <T> Result<T> unauthorized() {
        log.warn("业务异常响应: 401 UNAUTHORIZED");
        return new Result<>(ResultStatus.AUTH_FAIL.getCode(), ResultStatus.AUTH_FAIL.getMessage(), false);
    }

    public static <T> Result<T> unauthorized(T url) {
        log.warn("[{}] 业务异常响应: 401 UNAUTHORIZED {}", AuthContext.getTraceId(), url);
        return new Result<>(ResultStatus.AUTH_FAIL.getCode(), ResultStatus.AUTH_FAIL.getMessage(), false, url);
    }

    public static <T> Result<T> notfound() {
        log.warn("业务异常响应: 404 NOTFOUND");
        return new Result<>(ResultStatus.NOTFOUND.getCode(), ResultStatus.NOTFOUND.getMessage(), false);
    }

    public static <T> Result<T> notfound(String message) {
        log.warn("业务异常响应: 404 NOTFOUND {}", message);
        return new Result<>(ResultStatus.NOTFOUND.getCode(), message, false);
    }


    public static <T> Result<T> noToken() {
        log.warn("业务异常响应: 401 NOT TOKEN");
        return new Result<>(ResultStatus.NO_TOKEN.getCode(), ResultStatus.NO_TOKEN.getMessage(), false);
    }

    public static <T> Result<T> illegalRequest() {
        log.warn("[{}] 业务异常响应: 20002 非法请求", AuthContext.getTraceId());
        return new Result<>(ResultStatus.ILLEGAL_REQUEST.getCode(), ResultStatus.ILLEGAL_REQUEST.getMessage(), false);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Result<?> other = (Result<?>) obj;
        if (result == null) {
            if (other.result != null) {
                return false;
            }
        } else if (!result.equals(other.result)) {
            return false;
        }
        if (message == null) {
            if (other.message != null) {
                return false;
            }
        } else if (!message.equals(other.message)) {
            return false;
        }
        if (code == null) {
            return other.code == null;
        } else {
            return code.equals(other.code);
        }
    }
}
