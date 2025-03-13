package com.tencent.wxcloudrun.common.security.token.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.common.security.token.model.FrontToken;
import com.tencent.wxcloudrun.common.security.token.store.RedisTokenStore;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * <p>
 * 用户令牌处理器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Component
public class FrontTokenHandler extends BaseTokenHandler {

    private final Boolean resetTokenConfig = false;
    @Resource
    private RedisTokenStore redisTokenStore;

    public Boolean check(String token, HttpServletRequest request) {
        // 可做用户IP黑白名单校验
        return redisTokenStore.validFrontToken(token);
    }

    /**
     * 创建用户Token
     *
     * @param userId    用户ID
     * @param expiresIn 令牌有效期（秒）
     * @return adminToken
     */
    public FrontToken createToken(Long userId, Long expiresIn, boolean resetToken) {
        FrontToken frontTokenValue = redisTokenStore.getFrontTokenValue(userId);
        if (ObjectUtil.isNull(frontTokenValue) || StrUtil.isBlank(frontTokenValue.getAccessToken())) {
            redisTokenStore.clearFrontRefreshToken(userId);
            return createNewToken(userId, expiresIn);
        }
        frontTokenValue.setExpiresIn(expiresIn);
        if (resetToken) {
            frontTokenValue.setAccessToken(IdUtil.fastUUID());
            frontTokenValue.setRefreshToken(IdUtil.fastUUID());
            redisTokenStore.deleteFrontToken(userId);
        }
        boolean store = redisTokenStore.storeFrontToken(frontTokenValue);
        if (!store) {
            throw BusinessException.exception("Token创建异常");
        }
        return frontTokenValue;
    }

    /**
     * 创建令牌
     *
     * @param userId    用户id
     * @param expiresIn 过期时间
     * @return
     */
    public FrontToken createToken(Long userId, Long expiresIn) {
        return createToken(userId, expiresIn, resetTokenConfig);
    }


    /**
     * 创建用户Token
     *
     * @param userId    用户ID
     * @param expiresIn 令牌有效期（秒）
     * @return adminToken
     */
    private FrontToken createNewToken(Long userId, Long expiresIn) {
        String accessToken = IdUtil.fastUUID();
        String refreshToken = IdUtil.fastUUID();
        log.info("\n创建用户ID[{}]令牌:\n a:{} r:{}", userId, accessToken, refreshToken);
        FrontToken frontToken = new FrontToken();
        frontToken.setUserId(userId);
        frontToken.setExpiresIn(expiresIn);
        frontToken.setAccessToken(accessToken);
        frontToken.setRefreshToken(refreshToken);
        boolean store = redisTokenStore.storeFrontToken(frontToken);
        if (!store) {
            throw BusinessException.exception("Token创建异常");
        }
        return frontToken;
    }

    public boolean deleteTokenByDeveloperId(Long userId) {
        return redisTokenStore.deleteFrontToken(userId);
    }
}
