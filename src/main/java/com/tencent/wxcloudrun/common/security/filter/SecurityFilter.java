package com.tencent.wxcloudrun.common.security.filter;

import cn.hutool.core.util.IdUtil;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import com.tencent.wxcloudrun.utils.IpUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Objects;

/**
 * <p>
 * 请求响应过滤器（脱敏/加密/过滤）
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Component
public class SecurityFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String traceId = "%s%s".formatted(System.currentTimeMillis(), IdUtil.getSnowflakeNextId());
        AuthContext.setTraceId(traceId);
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 获取请求 IP
        String requestIp = IpUtil.getRealIp(servletRequest);


        // URL 参数打印
        Enumeration<String> parameterNames = servletRequest.getParameterNames();
        StringBuilder params = new StringBuilder();
        while (parameterNames.hasMoreElements()) {
            if (params.isEmpty()) {
                params.append("?");
            } else {
                params.append("&");
            }
            String paramName = parameterNames.nextElement();
            String paramValue = servletRequest.getParameter(paramName);
            params.append(paramName).append("=").append(paramValue);
        }
        log.info("[{}] [{}] [{}] [{}{}]",
                traceId,
                requestIp,
                servletRequest.getMethod(),
                servletRequest.getRequestURI(),
                params
        );

        // 暂时不处理，直接返回
        filterChain.doFilter(request, response);
        String id = Objects.nonNull(AuthContext.tryGetAdminId()) ?
                "AID[%s]".formatted(AuthContext.tryGetAdminId()) :
                Objects.nonNull(AuthContext.tryGetUserId()) ? "UID[%s]".formatted(AuthContext.tryGetUserId()) : "";
        log.info("[{}] STATUS[{}] {}", traceId, httpServletResponse.getStatus(), id);
        // 清除线程中的用户信息，防止特殊情况下线程复用导致错误。
        AuthContext.clearContextUser();
    }
}
