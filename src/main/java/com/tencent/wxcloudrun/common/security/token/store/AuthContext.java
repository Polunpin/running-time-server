package com.tencent.wxcloudrun.common.security.token.store;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

/**
 * 线程会话缓存/存储线程中的用户信息 ThreadLocal Util
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
public class AuthContext {
    private static final ThreadLocal<String> TraceId = new ThreadLocal<>();
    private static final ThreadLocal<ContextUser> CONTEXT_USER = new ThreadLocal<>();


    /**
     * 设置链路ID
     *
     * @param traceId 链路ID
     */
    public static void setTraceId(String traceId) {
        TraceId.set(traceId);
    }

    /**
     * 获取链路ID
     */
    public static String getTraceId() {
        return TraceId.get();
    }


    public static void clearContextUser() {
//        log.info("\n 清除线程中的用户信息");
        CONTEXT_USER.remove();
    }

    /**
     * 获取登录用户信息
     *
     * @return
     */
    public static ContextUser getContextUser() {
        //System.out.println("当前线程：" + Thread.currentThread().getName());
        return CONTEXT_USER.get();
    }

    /**
     * 设置用户信息
     *
     * @param contextUser
     */
    public static void setContextUser(ContextUser contextUser) {
        CONTEXT_USER.set(contextUser);
    }

    /**
     * 获取登录用户信息
     *
     * @return ID or exception
     */
    public static ContextUser getContextUserThrow() {
        //System.out.println("当前线程：" + Thread.currentThread().getName());
        ContextUser contextUser = getContextUser();
        if (ObjectUtil.isNull(contextUser)) {
            throw BusinessException.exception(401, "请先登录");
        }
        return contextUser;
    }

    /**
     * 获取管理员ID如果不存在会抛401异常
     *
     * @return ID or exception
     */
    public static Long getAdminId() {
        Long adminId = getContextUser().getAdminId();
        if (ObjectUtil.isNull(adminId)) {
            throw BusinessException.exception(401, "请先登录");
        }
        return adminId;
    }

    /**
     * 获取用户ID如果不存在会抛401异常
     *
     * @return ID or exception
     */
    public static Long getUserId() {
        ContextUser contextUser = getContextUser();
        if (Objects.isNull(contextUser)) {
            throw BusinessException.exception(401, "请先登录");
        }
        Long userId = contextUser.getUserId();
        if (ObjectUtil.isNull(userId)) {
            throw BusinessException.exception(401, "请先登录");
        }
        return userId;
    }

    /**
     * 获取用户ID，允许为空
     *
     * @return null or id
     */
    public static Long tryGetUserId() {
        ContextUser contextUser = getContextUser();
        if (Objects.isNull(contextUser)) {
            return null;
        }
        return contextUser.getUserId();
    }

    /**
     * 获取管理员ID，允许为空
     *
     * @return null or id
     */
    public static Long tryGetAdminId() {
        ContextUser contextUser = getContextUser();
        if (Objects.isNull(contextUser)) {
            return null;
        }
        return contextUser.getAdminId();
    }


    /**
     * 检查当前登录的对象是否有某个权限
     *
     * @param permission 权限编码
     * @return 是否有此权限
     */
    public static boolean hasPermission(String permission) {
        ContextUser contextUser = getContextUserThrow();
        if (CollUtil.isEmpty(contextUser.getPermissions())) {
            return false;
        }
        return contextUser.getPermissions().contains(permission);
    }
}
