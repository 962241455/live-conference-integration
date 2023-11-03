package com.cmnt.dbpick.user.api.params;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Auther:
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneLoginContext {

    private UserPhoneLoginParams params;

    private String version;
    private String uid;
    private String mDeviceId;
}
