/*
 * Copyright (C) 2022 Guang zhou jing ji zhi guang ke ji you xian gong si.
 * All rights reserved.
 * Official Web Site: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 获取Body中的ID
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Getter
@Setter
@Schema(description = "分页请求参数")
public class IdBody<T> {
    @NotNull(message = "ID不能为空")
    @Schema(description = "ID", nullable = true)
    private T id;
}
