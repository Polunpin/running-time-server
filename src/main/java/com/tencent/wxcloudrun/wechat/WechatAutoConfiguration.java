package com.tencent.wxcloudrun.wechat;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * aws 自动配置类
 *
 * @author Houcloud
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({WechatProperties.class})
public class WechatAutoConfiguration {

    private final WechatProperties wechatMpProperties;
}
