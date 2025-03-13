package com.tencent.wxcloudrun.common.security.token.handler;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.common.security.token.model.AdminToken;
import com.tencent.wxcloudrun.common.security.token.store.RedisTokenStore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 管理员令牌处理器
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Component
public class AdminTokenHandler extends BaseTokenHandler {

    @Resource
    private RedisTokenStore redisTokenStore;


    public void updatePermission(Long adminId, List<String> permissions) {
        // 通过id查找原来的token
        AdminToken adminTokenValue = redisTokenStore.getAdminTokenValue(adminId);
        if (ObjectUtil.isNull(adminTokenValue) || StrUtil.isBlank(adminTokenValue.getAccessToken())) {
            return;
        }
        adminTokenValue.setPermissions(permissions);
        redisTokenStore.storeAdminToken(adminTokenValue);
    }

    /**
     * 创建管理员令牌
     *
     * @param adminId   id
     * @param expiresIn 令牌有效期（秒）
     * @return adminToken
     */
    public AdminToken createToken(Long adminId, Long expiresIn, List<String> permissions, boolean resetToken) {
        // 通过id查找原来的token
        AdminToken adminTokenValue = redisTokenStore.getAdminTokenValue(adminId);
        if (ObjectUtil.isNull(adminTokenValue) || StrUtil.isBlank(adminTokenValue.getAccessToken())) {
            redisTokenStore.clearAdminRefreshToken(adminId);
            return createNewToken(adminId, expiresIn, permissions);
        }
        // 设置新的过期时间和权限
        adminTokenValue.setExpiresIn(expiresIn);
        adminTokenValue.setPermissions(permissions);
        // 如果重置token
        if (resetToken) {
            // 重新生成token
            adminTokenValue.setAccessToken(IdUtil.fastUUID());
            adminTokenValue.setRefreshToken(IdUtil.fastUUID());
            // 删除所有Token
            redisTokenStore.deleteAdminToken(adminId);
        }
        boolean store = redisTokenStore.storeAdminToken(adminTokenValue);
        if (!store) {
            throw BusinessException.exception("Token创建异常");
        }
        return adminTokenValue;
    }

    private AdminToken createNewToken(Long adminId, Long expiresIn, List<String> permissions) {
        String accessToken = IdUtil.fastUUID();
        String refreshToken = IdUtil.fastUUID();
        log.info("\n创建管理员ID[{}]令牌:\n a:{} r:{}", adminId, accessToken, refreshToken);
        AdminToken adminToken = new AdminToken();
        adminToken.setAdminId(adminId);
        adminToken.setExpiresIn(expiresIn);
        adminToken.setAccessToken(accessToken);
        adminToken.setRefreshToken(refreshToken);
        adminToken.setPermissions(permissions);
        boolean store = redisTokenStore.storeAdminToken(adminToken);
        if (!store) {
            throw BusinessException.exception("Token创建异常");
        }
        return adminToken;
    }

    public boolean deleteTokenByAdminId(Long adminId) {
        return redisTokenStore.deleteAdminToken(adminId);
    }


    /**
     * 检查Token是否有效
     *
     * @param token
     * @return
     */
    public Boolean check(String token) {
        return redisTokenStore.validAdminToken(token);
    }
}
