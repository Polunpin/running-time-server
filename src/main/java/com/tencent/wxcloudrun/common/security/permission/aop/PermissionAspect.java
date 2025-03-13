package com.tencent.wxcloudrun.common.security.permission.aop;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.common.security.permission.annotation.Permission;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import com.tencent.wxcloudrun.common.security.token.store.ContextUser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 权限APO拦截
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Aspect
@Component
public class PermissionAspect {

    @Pointcut(value = "@annotation(permission)")
    public void toVerifyPermission(Permission permission) {
    }

    @Around(value = "toVerifyPermission(permission)", argNames = "pjp,permission")
    public Object deBefore(ProceedingJoinPoint pjp, Permission permission) throws Throwable {
        String[] codeArray = permission.value();
        log.info("验证权限: {}", (Object) codeArray);
        if (ArrayUtil.isNotEmpty(codeArray)) {
            ArrayList<String> scopeCodes = CollUtil.toList(codeArray);
            ContextUser contextUser = AuthContext.getContextUser();
            if (ObjectUtil.isNull(contextUser)) {
                log.warn("当前凭证为空");
                throw BusinessException.exception(403, "无权访问");
            }
            if (CollUtil.isEmpty(contextUser.getPermissions())) {
                log.warn("当前凭证权限为空");
                throw BusinessException.exception(403, "无权访问");
            }
            if (!CollUtil.containsAny(contextUser.getPermissions(), scopeCodes)) {
                log.warn("当前凭证没有 {} 权限", scopeCodes);
                throw BusinessException.exception(403, "无权访问");
            }
        }
        Object proceed = pjp.proceed();
        return proceed;
    }
}

