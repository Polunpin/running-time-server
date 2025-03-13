package com.tencent.wxcloudrun.common.security.interceptor;

import com.google.common.collect.Lists;
import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.config.SecurityProperties;
import com.tencent.wxcloudrun.common.security.token.handler.AdminTokenHandler;
import com.tencent.wxcloudrun.common.security.utils.HttpResultUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;


/**
 * <p>
 * 管理员Token拦截器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
public class AdminTokenInterceptor implements HandlerInterceptor {

    @Resource
    private AdminTokenHandler adminTokenHandler;

    @Resource
    private SecurityProperties securityProperties;

    @Getter
    private final List<UrlInfo> adminIgnores = Lists.newArrayList(
            UrlInfo.build("post", "/api/admin/login"),
            UrlInfo.build("delete", "/api/admin/logout")
    );

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, HttpServletResponse response, @NonNull Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        log.info("ADMIN REQUEST {} - {}", request.getMethod(), request.getRequestURI());
        // Token是否有效
        String token = adminTokenHandler.getBearerToken(request);
        // 处理忽略认证请求
        boolean isIgnore = PatternMatcher.match(request, adminIgnores);
        log.info("isIgnore{}", isIgnore);
        // 没有token + 又不是忽略列表
        if ((token == null || token.isEmpty()) && !isIgnore) {
            HttpResultUtils.out(Result.noToken(), response);
            return false;
        }
        Boolean valid = adminTokenHandler.check(token);
        // token无效 + 不是忽略
        if (!valid && !isIgnore) {
            HttpResultUtils.out(Result.unauthorized(), response);
            return false;
        }
        // 不是忽略 + 演示环境 + 不是GET方法
        if (!isIgnore && securityProperties.getDemoMode() && !request.getMethod().equalsIgnoreCase("get")) {
            HttpResultUtils.out(Result.noPrivilege("演示环境无权操作"), response);
            log.info("演示环境无法使用GET除外的请求");
            return false;
        }
        return true;
    }

}
