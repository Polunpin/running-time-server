/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.security.interceptor;

/**
 * <p>
 * 拦截器请求信息
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
public class UrlInfo {
    private String uri;

    private String method;

    public UrlInfo(String uri, String method) {
        this.uri = uri;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public static UrlInfo build(String method, String uri) {
        return new UrlInfo(uri, method);
    }

    @Override
    public String toString() {
        return method + " - " + uri;
    }
}
