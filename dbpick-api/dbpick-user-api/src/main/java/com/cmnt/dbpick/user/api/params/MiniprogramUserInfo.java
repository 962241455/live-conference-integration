package com.cmnt.dbpick.user.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther:
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniprogramUserInfo implements Serializable {

    private static final long serialVersionUID = 7582337201154396561L;

    private String encryptedData;
    private String iv;
}
