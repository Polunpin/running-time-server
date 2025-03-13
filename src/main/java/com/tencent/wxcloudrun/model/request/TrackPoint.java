/*
 * Copyright (c) 2024 广州境际之光科技有限公司.
 * Guangzhou Jingji Zhiguang Technology Co., All rights reserved.
 * Official website: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

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
public class TrackPoint {
    private Double latitude;
    private Double longitude;
    private Double speed;
    private Double altitude;
    private Integer hr;
}
