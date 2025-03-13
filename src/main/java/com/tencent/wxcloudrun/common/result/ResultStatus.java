package com.tencent.wxcloudrun.common.result;

/**
 * @author Houcloud
 */

public enum ResultStatus {

    /**
     * Http返回状态码
     */
    FAIL(0, "失败"),
    SUCCESS(1, "成功"),
    AUTH_FAIL(401, "授权凭证已失效"),
    NO_TOKEN(401, "访问需要授权凭证"),
    ACCESS_FAIL(403, "无权访问"),

    NOTFOUND(404, "数据不存在"),
    FREQUENT_OPERATION(20001, "操作频繁"),
    ILLEGAL_REQUEST(20002, "非法请求"),
    SERVICE_BUSY(500, "系统繁忙");


    private final Integer code;
    private final String message;

    ResultStatus(Integer code, String message1) {
        this.code = code;
        this.message = message1;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
