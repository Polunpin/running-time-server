package com.tencent.wxcloudrun.controller;

import com.tencent.wxcloudrun.common.result.Result;
import com.tencent.wxcloudrun.common.security.token.store.AuthContext;
import com.tencent.wxcloudrun.model.entity.User;
import com.tencent.wxcloudrun.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>
 * 用户接口
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024-12-19
 */
@Tag(name = "个人中心接口")
@RestController
@RequestMapping("/api/front")
public class PersonalController {

    @Resource
    private IUserService userService;

    @Operation(summary = "获取用户详情")
    @GetMapping("/profile")
    public Result<User> getUser() {
        Long userId = AuthContext.getUserId();
        User user = userService.getById(userId);
        if (Objects.isNull(user)) {
            return Result.notfound("用户未找到");
        }
        return Result.success(user);
    }

    @Operation(summary = "修改用户信息")
    @PutMapping("/profile")
    public Result<User> getUser(@RequestBody User user) {
        Long userId = AuthContext.getUserId();
        boolean updated = userService.lambdaUpdate().eq(User::getId, userId)
                .set(User::getAvatar, user.getAvatar())
                .set(User::getNickname, user.getNickname())
                .update();
        return updated ? Result.success(user) : Result.fail();
    }
}
