package com.tencent.wxcloudrun.common.security.token.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 面向管理员颁发的Token
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AdminToken extends Token implements Serializable {

    @Serial
    private static final long serialVersionUID = 3725023857981759780L;
    @JsonProperty("admin_id")
    private Long adminId;

    @JsonIgnore
    private List<String> permissions;
}
