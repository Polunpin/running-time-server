package com.tencent.wxcloudrun.common.security.interceptor.authorize;


import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

}
