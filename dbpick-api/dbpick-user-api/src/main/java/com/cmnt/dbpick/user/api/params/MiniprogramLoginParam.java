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
public class MiniprogramLoginParam implements Serializable {

    private static final long serialVersionUID = -5625485950336115828L;

    private String code;
    private MiniprogramUserInfo userInfo;

}
