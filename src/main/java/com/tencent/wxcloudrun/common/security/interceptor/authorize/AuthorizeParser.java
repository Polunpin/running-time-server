/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.security.interceptor.authorize;

import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.security.interceptor.FrontTokenAuthorizer;
import com.tencent.wxcloudrun.common.security.interceptor.UrlInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 验证注解解析器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AuthorizeParser implements InitializingBean {
    private final WebApplicationContext applicationContext;
    @Getter
    @Setter
    private List<UrlInfo> authorizeUrls = new ArrayList<>();

    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        handlerMethods.keySet().forEach(info -> {
            HandlerMethod handlerMethod = handlerMethods.get(info);
            // 获取方法上的注解
            Authorize ignoreAuth = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), Authorize.class);
            Optional.ofNullable(ignoreAuth)
                    .ifPresent(auth -> info.getPatternValues()
                            .forEach(url -> {
                                info.getMethodsCondition().getMethods().forEach(m -> {
                                    authorizeUrls.add(UrlInfo.build(m.name(), url));
                                });
                            }));

            // 获取类上的注解
            Authorize controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), Authorize.class);
            Optional.ofNullable(controller).ifPresent(inner -> info.getPatternValues()
                    .forEach(url -> {
                        info.getMethodsCondition().getMethods().forEach(m -> {
                            authorizeUrls.add(UrlInfo.build(m.name(), url));
                        });
                    }));
        });
        System.out.println("用户端需要验证身份的入口:");
        authorizeUrls.forEach(u -> System.out.println(StrUtil.format("{}-{}", u.getMethod(), u.getUri())));
        System.out.println("\n\n");
        FrontTokenAuthorizer.addAuthorizeUrls(authorizeUrls);
    }

}
