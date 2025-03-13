package com.tencent.wxcloudrun.common.security.interceptor;

import cn.hutool.core.util.StrUtil;
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
 * 乐观拦截器，只拦截需要拦截的请求（适用于开放请求较多，验证身份请求较少的情况）
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
public class FrontTokenAuthorizer implements HandlerInterceptor {

    @Getter
    @Setter
    private static List<UrlInfo> authorizeUrl = Lists.newArrayList();

    public static void addAuthorizeUrls(List<UrlInfo> ignoreInfos) {
        authorizeUrl.addAll(ignoreInfos);
    }

    @Resource
    private FrontTokenHandler frontTokenHandler;


    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        String token = frontTokenHandler.getBearerToken(request);
        boolean isMatcher = PatternMatcher.match(request, authorizeUrl);
        log.info("FRONT REQUEST {} - {}", request.getMethod(), request.getRequestURI());

        // 没有令牌 + 没保护
        if (StrUtil.isBlank(token) && !isMatcher) {
            log.info("无令牌，无保护");
            return true;
        }
        // 没有令牌 + 有保护
        if (StrUtil.isBlank(token) && isMatcher) {
            log.info("无令牌，有保护");
            HttpResultUtils.out(Result.unauthorized(request.getMethod() + request.getRequestURI()), response);
            return false;
        }

        // 有令牌无论有没有保护都解析令牌内容
        if (StrUtil.isNotBlank(token)) {
            if (isMatcher) {
                log.info("有令牌，有保护");
            } else {
                log.info("有令牌，无保护");
            }
            Boolean result = frontTokenHandler.check(token, request);
            if (!result && isMatcher) {
                HttpResultUtils.out(Result.unauthorized(request.getMethod() + request.getRequestURI()), response);
                return false;
            }
        }
        return true;
    }


}
