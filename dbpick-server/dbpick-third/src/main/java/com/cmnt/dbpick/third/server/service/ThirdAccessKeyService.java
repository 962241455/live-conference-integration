package com.cmnt.dbpick.third.server.service;


import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.third.api.params.EditStatusParam;

public interface ThirdAccessKeyService {

    /**
     * 获取访问凭证
     * @param thirdId 商户id
     * @return
     */
    ThirdAccessKeyVO findAccessKeyByUser(String thirdId);

    /**
     * 生成访问凭证
     * @param thirdId 商户id
     * @param userId  创建用户id
     * @return
     */
    ThirdAccessKeyVO createAccess(String thirdId, String userId);

    /**
     * 更新访问凭证
     * @return
     */
    ThirdAccessKeyVO updateAccess(EditStatusParam param);

    /**
     * 校验ak sk 是否可用
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    ThirdAccessKeyVO existsAccess(String accessKeyId, String accessKeySecret);
}
