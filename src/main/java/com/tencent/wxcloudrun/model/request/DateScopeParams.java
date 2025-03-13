/*
 * Copyright (C) 2022 Yunhou·Huang  yunhouhuang@gmail.com.
 * All rights reserved.
 * Official Web Site: http://houcloud.com.
 */

package com.tencent.wxcloudrun.model.request;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 用来接收前端的时间范围
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
public class DateScopeParams {
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startAt;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endAt;

    public <T> void formatWrapper(LambdaQueryWrapper<T> wrapper, SFunction<T, ?> column) {
        // 时间范围筛选
        if (Objects.nonNull(this.startAt)) {
            if (Objects.isNull(this.endAt)) {
                this.endAt = LocalDateTime.now();
            }
            wrapper.between(column, this.startAt, this.startAt);
        }
    }
}
