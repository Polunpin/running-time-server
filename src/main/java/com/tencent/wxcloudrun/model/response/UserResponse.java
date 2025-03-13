/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * mark
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024/12/22
 */
@Data
public class UserResponse {

    private Long id;
    private String avatar;
    private String nickname;
    private LocalDateTime createdAt;
}
