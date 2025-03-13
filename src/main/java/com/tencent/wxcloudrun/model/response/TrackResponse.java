/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.response;

import com.baomidou.mybatisplus.annotation.*;
import com.tencent.wxcloudrun.model.request.TrackPoint;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 轨迹响应对象
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024/12/22
 */
@Data
public class TrackResponse {

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "跑步距离")
    private Double distance;

    @Schema(description = "跑步时长")
    private Double duration;

    @Schema(description = "最高海拔")
    private Double maxAltitude;

    @Schema(description = "分享到微信运动的时间")
    private LocalDateTime werunSharedAt;

    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    @Schema(description = "轨迹点JSON")
    private List<TrackPoint> points;
    private UserResponse user;
}
