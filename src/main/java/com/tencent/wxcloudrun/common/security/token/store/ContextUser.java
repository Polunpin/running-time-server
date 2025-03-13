package com.tencent.wxcloudrun.common.security.token.store;

import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 抽象用户
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
public class ContextUser {
    private static final String ROLE = "ROLE_";
    private static final String NA = "N/A";

    /**
     * 管理员ID（当ContextUser的类型为 管理员是不为空）
     */
    private Long adminId;

    /**
     * 用户ID（当ContextUser的类型为 用户不为空）
     */
    private Long userId;

    private List<String> permissions;

    public static List<String> getRoles(ContextUser contextUser) {
        List<String> permissions = contextUser.getPermissions();
        List<String> roleTags = new ArrayList<>();
        permissions.stream()
                .filter(granted -> StrUtil.startWith(granted, ROLE))
                .forEach(roleTags::add);
        return roleTags;
    }
}
