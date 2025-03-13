package com.tencent.wxcloudrun.common.security.token.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 面向客户端颁发的Token
 * </p>
 *
 * @author <a href="mailto:yunhouhuang@gmail.com">yunhouhuang@gmail.com</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ClientToken extends Token {

    @JsonProperty("client_id")
    private String clientId;

    private List<String> scope;

    private List<String> permissions;
}
