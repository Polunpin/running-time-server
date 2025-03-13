package com.tencent.wxcloudrun.wechat.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * 授权码校验响应结果
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class WeappAuthorizeResponse {
    private String session_key;
    private String openid;
    private String unionid;
    private String errcode;
    private String errmsg;
}