package com.tencent.wxcloudrun.common.security.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * Token基础
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
public class Token implements Serializable {
    public static final String ACCESS_TOKEN = "access_token";
    public static final String REFRESH_TOKEN = "refresh_token";
    @Serial
    private static final long serialVersionUID = -7615128450772956272L;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;
    /**
     * 过期时间(秒)
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

}
