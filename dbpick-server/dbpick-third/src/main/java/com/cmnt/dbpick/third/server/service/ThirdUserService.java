package com.cmnt.dbpick.third.server.service;

import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.api.params.ThirdUserEditParam;
import com.cmnt.dbpick.third.api.vo.ThirdUserLoginVO;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;

public interface ThirdUserService {

    /**
     * 生成商户子账号
     * @param param
     */
    ThirdUserLoginVO createThirdUser(ThirdUserEditParam param);

    /**
     * 更新商户子账号
     */
    ThirdUserLoginVO editThirdUser(ThirdUserEditParam param);

    /**
     * 根据用户id查询登录信息
     * @param userId
     * @return
     */
    ThirdUser findThirdUserByUserId(String userId);

    /**
     * 商户后台生成用户登录token
     * @param vo
     * @return
     */
    ThirdUser createLoginToken(ThirdUserLoginVO vo) throws Exception;

    /**
     * 根据商户账号状态，更新子账号状态
     * @return
     */
    boolean disableUserByThirdId(EditStatusParam param);
}
