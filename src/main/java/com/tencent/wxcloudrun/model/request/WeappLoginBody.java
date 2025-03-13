/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * mark
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024/12/19
 */
@Data
public class WeappLoginBody {
    @Schema(description = "授权码")
    private String code;
}
