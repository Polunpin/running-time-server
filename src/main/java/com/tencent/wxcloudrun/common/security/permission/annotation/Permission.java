package com.tencent.wxcloudrun.common.security.permission.annotation;

import java.lang.annotation.*;

/**
 * 权限注解支持EL表达式
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {

    /**
     * 权限编码
     *
     * @return !
     */
    String[] value() default {};


    /**
     * 服务类型 {@link ServerType}
     *
     * @return !
     */
    ServerType server() default ServerType.UNKNOWN;

    /**
     * 权限名称
     *
     * @return !
     */
    String name() default "";
}

