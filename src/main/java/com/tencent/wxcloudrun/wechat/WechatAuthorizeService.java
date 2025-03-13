
package com.tencent.wxcloudrun.wechat;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.tencent.wxcloudrun.common.exception.BusinessException;
import com.tencent.wxcloudrun.wechat.model.WeappAuthorizeResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * <p>
 * mark
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@Service
public class WechatAuthorizeService {
    public static final String WeappAuthCodeUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

    @Resource
    private WechatProperties wechatProperties;

    /**
     * 认证解析小程序授权码
     *
     * @param code 小程序授权码
     * @return openid
     */
    public WeappAuthorizeResponse authorizeCode(String code) {
        String url = String.format(WeappAuthCodeUrl, wechatProperties.getWeapp().getAppid(), wechatProperties.getWeapp().getAppSecret(), code);
        String responseJson = HttpUtil.get(url);
        if (StrUtil.isBlank(responseJson)) {
            throw BusinessException.exception("授权码请求错误");
        }
        WeappAuthorizeResponse response = JSONObject.parseObject(responseJson, WeappAuthorizeResponse.class);
        if (Objects.isNull(response) || StrUtil.isBlank(response.getOpenid())) {
            log.error("解析微信小程序授权码失败: {}", response);
        }
        return response;
    }

}
