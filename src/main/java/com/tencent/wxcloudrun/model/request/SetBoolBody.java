/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * mark
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SetBoolBody<ID> extends IdBody<ID> {

    @Schema(description = "布尔值")
    @NotNull(message = "请指定布尔值")
    private Boolean value;
}
