package com.tencent.wxcloudrun.common.security.permission.annotation;

/**
 * <p>
 * Explain
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
public enum ServerType {
    /**
     *
     */
    UNKNOWN("unknown", "未定义"),
    FRONT("front", "用户端"),
    ADMIN("admin", "管理后台");

    ServerType(String code, String name) {

    }

}
