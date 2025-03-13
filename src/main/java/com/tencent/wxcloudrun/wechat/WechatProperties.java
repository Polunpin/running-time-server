package com.tencent.wxcloudrun.wechat;

import com.tencent.wxcloudrun.wechat.model.AppIdSecret;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {
    private AppIdSecret weapp;
}
