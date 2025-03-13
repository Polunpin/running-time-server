/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * mark
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {
    private Boolean demoMode = false;


    private String adminPrefix = "/api/admin";

    private String frontPrefix = "/api/front";
}
