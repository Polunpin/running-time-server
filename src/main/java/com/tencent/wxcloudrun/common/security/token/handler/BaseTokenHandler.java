package com.tencent.wxcloudrun.common.security.token.handler;

import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.security.token.model.Token;
import jakarta.servlet.http.HttpServletRequest;


/**
 * <p>
 * 令牌处理器基类
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
public abstract class BaseTokenHandler {

    public String getTokenFormRequest(HttpServletRequest request) {
        String token = request.getHeader(Token.ACCESS_TOKEN);
        if (StrUtil.isBlank(token)) {
            token = request.getParameter(Token.ACCESS_TOKEN);
        }
        return token;
    }

    public String getBearerToken(HttpServletRequest request) {
        String type = "Bearer ";
        String token = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(token) && token.contains(type)) {
            return token.replace(type, "");
        }
        return token;
    }

}
