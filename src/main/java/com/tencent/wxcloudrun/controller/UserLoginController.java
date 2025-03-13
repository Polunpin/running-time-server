package com.tencent.wxcloudrun.controller;

import cn.hutool.core.util.IdUtil;
import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.token.handler.FrontTokenHandler;
import com.tencent.wxcloudrun.common.security.token.model.FrontToken;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.model.request.WeappLoginBody;
import com.tencent.wxcloudrun.service.IUserService;
import com.tencent.wxcloudrun.wechat.WechatAuthorizeService;
import com.tencent.wxcloudrun.wechat.model.WeappAuthorizeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * mark
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Slf4j
@RestController
@Tag(name = "用户登录")
@RequestMapping("/api/front")
public class UserLoginController {

    @Resource
    private IUserService userService;

    @Resource
    private FrontTokenHandler frontTokenHandler;

    @Resource
    private WechatAuthorizeService wechatAuthorizeService;

    /**
     * 微信快速登录
     *
     * @param body 请求提数据
     */
    @Operation(summary = "微信快速登录")
    @PostMapping("/login")
    public Result<FrontToken> login(@Valid @RequestBody WeappLoginBody body) {
        WeappAuthorizeResponse response = wechatAuthorizeService.authorizeCode(body.getCode());
        // 拿微信的openid去查数据库，看有没有这个用户
        User user = userService.lambdaQuery().eq(User::getWeappOpenid, response.getOpenid()).one();
        if (Objects.isNull(user)) {
            // 自动注册
            user = new User();
            user.setNickname("新用户" + IdUtil.nanoId(6));
            user.setAvatar("");
            user.setWeappOpenid(response.getOpenid());
            // 插到数据库
            boolean save = userService.save(user);
            if (!save){
                return Result.fail("登录失败");
            }
        }
        // 返回7天的令牌
        FrontToken token = frontTokenHandler.createToken(user.getId(), 60 * 60 * 24 * 7L);
        return Result.success(token);
    }

    @Operation(summary = "退出登录")
    @DeleteMapping("/logout")
    public Result<Void> logout() {
        Long userId = AuthContext.tryGetUserId();
        if (Objects.nonNull(userId)) {
            frontTokenHandler.deleteTokenByDeveloperId(userId);
        }
        return Result.success();
    }

}
