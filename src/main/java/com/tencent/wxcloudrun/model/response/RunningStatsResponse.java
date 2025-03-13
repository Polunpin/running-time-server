/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.response;

import lombok.Data;

/**
 * <p>
 * mark
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024/12/22
 */
@Data
public class RunningStatsResponse {

    private Double totalDistance;

    private Double todayDistance;

    private Long totalDays;
}
