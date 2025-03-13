package com.tencent.wxcloudrun.wechat.model;

import lombok.Data;

@Data
public class AppIdSecret {

    private String appid;
    private String appSecret;
    private String token;
    private String aesKey;
}
