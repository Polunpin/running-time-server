package com.tencent.wxcloudrun.common.security.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 面向用户颁发的Token
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FrontToken extends Token {

    @JsonProperty("user_id")
    private Long userId;

}
