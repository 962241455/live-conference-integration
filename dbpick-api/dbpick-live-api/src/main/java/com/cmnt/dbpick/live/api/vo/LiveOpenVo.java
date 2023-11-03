/**
 * Demo class
 *
 * @author 28021
 * @date 2022/7/25
 */
package com.cmnt.dbpick.live.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 直播私钥返回
 *
 * @author mr . wei
 * @date 2022/7/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveOpenVo  implements Serializable {

    private String pushUrl;
    private String pushUrlTimeOut;

    private String secret;

    private String playUrl;
}
