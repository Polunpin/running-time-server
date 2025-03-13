package com.tencent.wxcloudrun.common.constants;

import lombok.Getter;

/**
 * <p> 正则相关枚举 </p>
 *
 * @author Houcloud
 * @since 2020/5/11 11:11 下午
 */
@Getter
public enum RegexpEnums {
    /**
     * 邮箱正则校验
     */
    EMAIL_REGULAR("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$"),
    /**
     * 手机正则校验
     */
    MOBILE_REGULAR("^[1][345789]\\d{9}$");

    private final String content;

    RegexpEnums(String content) {
        this.content = content;
    }

}