/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * mark
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024/12/22
 */
@Data
public class UploadTrackBody {

    @NotNull
    private Double distance;

//    @NotNull
    private Double duration;

    private Double calorie;


    private LocalDateTime werunSharedAt;

    @NotEmpty
    private List<TrackPoint> points;
}
