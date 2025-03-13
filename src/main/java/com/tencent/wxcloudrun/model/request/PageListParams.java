/*
 * Copyright (C) 2022 Guang zhou jing ji zhi guang ke ji you xian gong si.
 * All rights reserved.
 * Official Web Site: http://www.lateotu.com.
 */

package com.tencent.wxcloudrun.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 通用列表分页参数
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Getter
@Setter
public class PageListParams extends PageParams {

    private String keywords;

}
