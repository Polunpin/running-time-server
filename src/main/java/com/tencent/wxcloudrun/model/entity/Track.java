package com.tencent.wxcloudrun.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 运动记录
 * </p>
 *
 * @author yunhouhuang@gmail.com
 * @since 2024-12-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_track")
@Schema(name = "Track", description = "运动记录")
public class Track extends Model<Track> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
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
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description = "删除时间")
    @TableLogic(value = "null", delval = "now()")
    @JsonIgnore
    private LocalDateTime deletedAt;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description = "轨迹点JSON")
    private String pointsJson;

    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
