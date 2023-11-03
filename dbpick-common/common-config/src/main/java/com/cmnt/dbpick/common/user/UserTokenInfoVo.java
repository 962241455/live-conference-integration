package com.cmnt.dbpick.common.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTokenInfoVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String access_token;
    private String token_type;
    private String refresh_token;
    private String expires_in;
    private String scope;


}
