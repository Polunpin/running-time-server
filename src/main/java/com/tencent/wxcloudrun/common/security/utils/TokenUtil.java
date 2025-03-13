package com.tencent.wxcloudrun.common.security.utils;

import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.security.token.handler.FrontTokenHandler;
import com.tencent.wxcloudrun.common.security.token.store.RedisTokenStore;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;


@Service
public class TokenUtil {

    @Resource
    private RedisTokenStore redisTokenStore;

    @Resource
    private FrontTokenHandler frontTokenHandler;

    public Long getUserIdByToken(String token) {
        return redisTokenStore.getUserIdByToken(token);
    }

    /**
     * 从header中提取token获取用户id
     *
     * @param request
     * @return
     */
    public Long getUserIdRequestHeader(HttpServletRequest request) {
        String bearerToken = frontTokenHandler.getBearerToken(request);
        if (StrUtil.isBlank(bearerToken)) {
            return null;
        }
        return redisTokenStore.getUserIdByToken(bearerToken);
    }

}
