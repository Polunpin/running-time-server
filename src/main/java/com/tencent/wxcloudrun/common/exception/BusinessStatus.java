package com.tencent.wxcloudrun.common.exception;

import lombok.Getter;

/**
 * <p> 异常状态 </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Getter
public enum BusinessStatus {
    /**
     * 管理员 - 账号密码错误
     */
    USERNAME_OR_PASSWORD_FAULT(100101, "账号或密码错误"),
    SERVICE_BUSY(500, "服务器繁忙"),
    UNKNOWN(-1, "未知错误"),
    SUCCESS(1, "成功"),
    ERROR(0, "失败"),
    ACCOUNT_STATUS_IS_FREEZE(2, "账户密码登录模式被禁用"),
    BREAKING(20002, "熔断"),
    ILLEGAL_REQUEST(50000, "非法请求"),
    REQUEST_DATA_IS_MISSING(50001, "请求参数错误"),
    CHECK_ERROR(50002, "参数校验错误"),
    ILLEGAL_TOKEN(50008, "非法令牌"),
    OTHER_CLIENTS_LOGGED_IN(50012, "其他客户登录"),
    AUTH_EXPIRED(50014, "认证已失效"),
    CLIENT_AUTH_FAIL(50017, "客户端认证失败"),
    UNSUPPORTED_GRANT_TYPE(401, "不支持的认证类型"),
    AUTH_FAIL(50015, "身份认证失败"),
    CODE_ERROR(50015, "验证码错误"),
    CODE_EXPIRED(50016, "验证码已过期"),
    CODE_NOT_NULL(50017, "验证码不能为空");

    public static final String SERVICE_STATUS_EXC = "服务状态不可用";
    private final Integer code;
    private final String message;

    BusinessStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static String getMessage(int code) {
        for (BusinessStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status.getMessage();
            }
        }
        return null;
    }

}
