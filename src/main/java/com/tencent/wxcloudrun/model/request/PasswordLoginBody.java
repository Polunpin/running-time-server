/*
 * Copyright (C) 2022 Guang zhou jing ji zhi guang ke ji you xian gong si.
 * All rights reserved.
 * Official Web Site: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 密码登录参数
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@Schema(description = "密码登录参数")
public class PasswordLoginBody {
    @NotBlank(message = "账号不能为空")
    @Schema(description = "账号", nullable = true)
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", nullable = true)
    private String password;
    @Schema(description = "验证码")
    private String verifyCode;
}
