/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.constants;

/**
 * <p>
 * 全局常量类
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
public class Constants {
    public static final String ONE_STR = "1";
    public static final String ZERO_STR = "0";
    /**
     * 菜单根ID
     */
    public static final Long ROOT_MENU_ID = 0L;

    /**
     * 已读状态
     */
    public static final String READ_STATUS = ONE_STR;

    /**
     * 未读状态
     */
    public static final String UNREAD_STATUS = ZERO_STR;

    /**
     * 类型为菜单
     */
    public static final String MENU_TYPE_MENU = ZERO_STR;
    /**
     * 类型为权限
     */
    public static final String MENU_TYPE_PERMISSION = ONE_STR;

    /**
     * 类型为接口日志
     */
    public static final String SYSLOG_API = ZERO_STR;

    /**
     * 类型为登录日志
     */
    public static final String SYSLOG_LOGIN = ONE_STR;


}
