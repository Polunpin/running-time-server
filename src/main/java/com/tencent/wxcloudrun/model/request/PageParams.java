package com.tencent.wxcloudrun.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 基础分页参数
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@Schema(description = "分页请求参数")
public class PageParams {

    @Schema(description = "页码", example = "1")
    private int page = 1;

    @Schema(description = "每页数量", example = "20")
    private int limit = 10;
}
