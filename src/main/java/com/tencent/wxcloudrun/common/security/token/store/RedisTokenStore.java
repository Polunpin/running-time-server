package com.tencent.wxcloudrun.common.security.token.store;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.tencent.wxcloudrun.common.security.token.model.AdminToken;
import com.tencent.wxcloudrun.common.security.token.model.ClientToken;
import com.tencent.wxcloudrun.common.security.token.model.FrontToken;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Token存储器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@AllArgsConstructor
public class RedisTokenStore {
    public static final String ADMIN_TOKEN_KEY = "ADMIN-AUTH:";
    public static final String FRONT_TOKEN_KEY = "DEVELOPER-AUTH:";
    public static final String CLIENT_TOKEN_KEY = "CLIENT-AUTH:";
    public static final String ADMIN_REFRESH_KEY = "ADMIN-REFRESH-KEY:";
    public static final String FRONT_REFRESH_KEY = "FRONT-REFRESH-KEY:";
    public static final String CLIENT_REFRESH_KEY = "CLIENT-REFRESH-KEY:";

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 存储管理员Token信息到Redis
     *
     * @param adminToken 管理员Token信息
     * @return bool
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean storeAdminToken(AdminToken adminToken) {
        Long adminId = adminToken.getAdminId();
        String redisTokenKey = ADMIN_TOKEN_KEY + adminId;
        String redisAccessToken = ADMIN_TOKEN_KEY + adminToken.getAccessToken();
        String redisRefreshToken = ADMIN_TOKEN_KEY + adminToken.getRefreshToken();
        long expiresIn = adminToken.getExpiresIn();
        long refreshExpiresIn = (expiresIn * 3);
        log.info("存储管理员令牌:{}", adminToken);
        String jsonStr = JSONUtil.toJsonStr(adminToken);
        // 存储 adminId -> accessToken
        stringRedisTemplate.opsForValue().set(redisTokenKey, redisAccessToken, expiresIn, TimeUnit.SECONDS);
        // 存储 adminId -> refreshToken
        stringRedisTemplate.opsForValue().set(ADMIN_REFRESH_KEY + adminId, redisRefreshToken, refreshExpiresIn, TimeUnit.SECONDS);
        // 存储 refreshToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisRefreshToken, jsonStr, refreshExpiresIn, TimeUnit.SECONDS);
        // 存储 accessToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisAccessToken, jsonStr, expiresIn, TimeUnit.SECONDS);
        return true;
    }


    @Transactional(rollbackFor = Exception.class)
    public boolean storeFrontToken(FrontToken frontToken) {
        Long userId = frontToken.getUserId();
        String redisTokenKey = FRONT_TOKEN_KEY + userId;
        String refreshKey = FRONT_REFRESH_KEY + frontToken.getUserId();
        String redisAccessToken = FRONT_TOKEN_KEY + frontToken.getAccessToken();
        String redisRefreshToken = FRONT_TOKEN_KEY + frontToken.getRefreshToken();
        long expiresIn = frontToken.getExpiresIn();
        log.info("存储用户令牌");
        String jsonStr = JSONUtil.toJsonStr(frontToken);
        // 存储 adminId -> accessToken
        stringRedisTemplate.opsForValue().set(refreshKey, redisRefreshToken, expiresIn * 3, TimeUnit.SECONDS);
        // 存储 clientId -> refreshToken
        stringRedisTemplate.opsForValue().set(redisTokenKey, redisAccessToken, expiresIn, TimeUnit.SECONDS);
        // 存储 refreshToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisRefreshToken, jsonStr, expiresIn * 3, TimeUnit.SECONDS);
        // 存储 accessToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisAccessToken, jsonStr, expiresIn, TimeUnit.SECONDS);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean storeClientToken(ClientToken clientToken, Long accessTokenExpiresIn, Long refreshTokenExpiresIn) {
        String clientId = clientToken.getClientId();
        String redisTokenKey = CLIENT_TOKEN_KEY + clientId;
        String redisAccessToken = CLIENT_TOKEN_KEY + clientToken.getAccessToken();
        String redisRefreshToken = CLIENT_TOKEN_KEY + clientToken.getRefreshToken();
        log.info("存储客户端令牌");
        String jsonStr = JSONUtil.toJsonStr(clientToken);
        // 存储 adminId -> accessToken
        stringRedisTemplate.opsForValue().set(redisTokenKey, redisAccessToken, accessTokenExpiresIn, TimeUnit.SECONDS);
        // 存储 clientId -> refreshToken
        stringRedisTemplate.opsForValue().set(CLIENT_REFRESH_KEY + clientToken.getClientId(), redisRefreshToken, refreshTokenExpiresIn, TimeUnit.SECONDS);

        // 存储 refreshToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisRefreshToken, jsonStr, refreshTokenExpiresIn, TimeUnit.SECONDS);
        // 存储 accessToken -> tokenVal
        stringRedisTemplate.opsForValue().set(redisAccessToken, jsonStr, accessTokenExpiresIn, TimeUnit.SECONDS);
        return true;
    }


    public Boolean validAdminToken(String token) {
        String accessToken = ADMIN_TOKEN_KEY + token;
        Boolean aBoolean = stringRedisTemplate.hasKey(accessToken);
        if (Objects.nonNull(aBoolean) && aBoolean) {
            String tokenValue = stringRedisTemplate.opsForValue().get(accessToken);
            AdminToken adminToken = JSONUtil.toBean(tokenValue, AdminToken.class);
            if (StrUtil.isBlank(tokenValue) || ObjectUtil.isNull(adminToken)) {
                log.warn("管理员令牌无效：tokenValue is Blank.");
            }
            ContextUser contextUser = new ContextUser();
            contextUser.setAdminId(adminToken.getAdminId());
            contextUser.setPermissions(adminToken.getPermissions());
            AuthContext.setContextUser(contextUser);
            return true;
        }
        return false;
    }

    public Boolean validFrontToken(String token) {
        String accessToken = FRONT_TOKEN_KEY + token;
        Boolean aBoolean = stringRedisTemplate.hasKey(accessToken);
        if (Objects.nonNull(aBoolean) && aBoolean) {
            String tokenValue = stringRedisTemplate.opsForValue().get(accessToken);
            FrontToken frontToken = JSONUtil.toBean(tokenValue, FrontToken.class);
            if (StrUtil.isBlank(tokenValue) || ObjectUtil.isNull(frontToken)) {
                log.warn("用户令牌无效：tokenValue is Blank.");
                return false;
            }
            ContextUser contextUser = new ContextUser();
            contextUser.setUserId(frontToken.getUserId());
            contextUser.setPermissions(Collections.emptyList());
            AuthContext.setContextUser(contextUser);
            return true;
        }
        return false;
    }

    public Long getUserIdByToken(String token) {

        String accessToken = FRONT_TOKEN_KEY + token;
        Boolean aBoolean = stringRedisTemplate.hasKey(accessToken);
        if (Objects.nonNull(aBoolean) && aBoolean) {
            String tokenValue = stringRedisTemplate.opsForValue().get(accessToken);
            FrontToken frontToken = JSONUtil.toBean(tokenValue, FrontToken.class);
            if (Objects.isNull(frontToken)) {
                return null;
            }
            return frontToken.getUserId();
        }
        return null;
    }

    public void clearAdminRefreshToken(Long adminId) {
        String key = ADMIN_REFRESH_KEY + adminId;
        String refreshToken = stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.delete(key);
        if (StrUtil.isNotBlank(refreshToken)) {
            assert refreshToken != null;
            stringRedisTemplate.delete(refreshToken);
        }
    }

    public boolean deleteAdminToken(Long adminId) {
        String key = ADMIN_TOKEN_KEY + adminId;
        String redisAccessToken = stringRedisTemplate.opsForValue().get(key);
        String key2 = ADMIN_REFRESH_KEY + adminId;
        stringRedisTemplate.delete(key2);
        stringRedisTemplate.delete(key);
        if (StrUtil.isBlank(redisAccessToken)) {
            log.warn("Admin redis AccessToken is blank");
            return true;
        }
        String adminTokenVal = stringRedisTemplate.opsForValue().get(redisAccessToken);
        stringRedisTemplate.delete(redisAccessToken);
        if (StrUtil.isBlank(adminTokenVal)) {
            return true;
        }
        AdminToken adminToken = JSONUtil.toBean(adminTokenVal, AdminToken.class);
        if (StrUtil.isNotBlank(adminToken.getRefreshToken())) {
            stringRedisTemplate.delete(ADMIN_TOKEN_KEY + adminToken.getRefreshToken());
        }
        return true;
    }

    public boolean deleteFrontToken(Long userId) {
        String key = FRONT_TOKEN_KEY + userId;
        String key2 = FRONT_REFRESH_KEY + userId;
        stringRedisTemplate.delete(key2);
        String redisAccessToken = stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.delete(key);
        if (StrUtil.isBlank(redisAccessToken)) {
            log.warn("Front redis AccessToken is blank");
            return true;
        }
        String tokenVal = stringRedisTemplate.opsForValue().get(redisAccessToken);
        stringRedisTemplate.delete(redisAccessToken);
        if (StrUtil.isBlank(tokenVal)) {
            return true;
        }
        FrontToken frontToken = JSONUtil.toBean(tokenVal, FrontToken.class);
        if (StrUtil.isNotBlank(frontToken.getRefreshToken())) {
            stringRedisTemplate.delete(FRONT_TOKEN_KEY + frontToken.getRefreshToken());
        }
        return true;
    }


    public void clearFrontRefreshToken(Long userId) {
        String key = ADMIN_REFRESH_KEY + userId;
        String refreshToken = stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.delete(key);
        if (StrUtil.isNotBlank(refreshToken)) {
            assert refreshToken != null;
            stringRedisTemplate.delete(refreshToken);
        }
    }


    public FrontToken getFrontTokenValue(Long userId) {
        String objToken = stringRedisTemplate.opsForValue().get(FRONT_TOKEN_KEY + userId);
        if (StrUtil.isNotBlank(objToken)) {
            assert objToken != null;
            String s = stringRedisTemplate.opsForValue().get(objToken);
            return JSONUtil.toBean(s, FrontToken.class);
        }
        return null;
    }

    public AdminToken getAdminTokenValue(Long adminId) {
        String objToken = stringRedisTemplate.opsForValue().get(ADMIN_TOKEN_KEY + adminId);
        if (StrUtil.isNotBlank(objToken)) {
            assert objToken != null;
            String s = stringRedisTemplate.opsForValue().get(objToken);
            return JSONUtil.toBean(s, AdminToken.class);
        }
        return null;
    }

    public <T> T getTokenValueByRefreshToken(String refreshToken, Class<T> tokenClass) {
        String objToken = stringRedisTemplate.opsForValue().get(refreshToken);
        if (StrUtil.isBlank(objToken)) {
            return null;
        }
        return JSONUtil.toBean(objToken, tokenClass);
    }

    public <T> T getTokenValueByAccessToken(String accessToken, Class<T> tokenClass) {
        String objToken = stringRedisTemplate.opsForValue().get(accessToken);
        if (StrUtil.isBlank(objToken)) {
            return null;
        }
        return JSONUtil.toBean(objToken, tokenClass);
    }

}
