package com.tencent.wxcloudrun.common.security.interceptor;

import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.token.handler.FrontTokenHandler;
import com.tencent.wxcloudrun.common.security.utils.HttpResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;


/**
 * <p>
 * 悲观拦截器，默认拦截所有请求，若不拦截则需要配置忽略请求列表
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
public class FrontTokenInterceptor implements HandlerInterceptor {

    @Getter
    @Setter
    private static List<UrlInfo> frontIgnores = Lists.newArrayList(
            UrlInfo.build("get", "/test")
    );

    public static void addIgnores(List<UrlInfo> ignoreInfos) {
        frontIgnores.addAll(ignoreInfos);
    }

    @Resource
    private FrontTokenHandler frontTokenHandler;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        String token = frontTokenHandler.getBearerToken(request);
        boolean isIgnore = PatternMatcher.match(request, frontIgnores);
        log.info("FRONT REQUEST {} - {}", request.getMethod(), request.getRequestURI());
        if ((token == null || token.isEmpty()) && !isIgnore) {
            HttpResultUtils.out(Result.unauthorized(request.getMethod() + request.getRequestURI()), response);
            return false;
        }
        Boolean result = frontTokenHandler.check(token, request);
        if (!result && !isIgnore) {
            HttpResultUtils.out(Result.unauthorized(request.getMethod() + request.getRequestURI()), response);
            return false;
        }
        return true;
    }


}
