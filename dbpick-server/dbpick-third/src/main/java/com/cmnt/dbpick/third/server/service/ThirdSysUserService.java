package com.cmnt.dbpick.third.server.service;


import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.ThirdSysUserVO;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdSysUser;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserEditParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserQueryParam;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;

public interface ThirdSysUserService {

    /**
     * 根据商户id查询商户账号
     * @param thirdId
     * @return
     */
    ThirdSysUser findSysThirdUserByThirdId(String thirdId);

    /**
     * 获取商户账号列表
     */
    PageResponse<ThirdSysUserVO> getSysThirdUserList(SysThirdUserQueryParam params);

    /**
     * 生成商户账号
     * @param param
     * @return
     */
    ThirdSysUserVO createSysThirdUser(SysThirdUserEditParam param);

    ThirdSysUserVO editSysThirdUser(SysThirdUserEditParam param);

    /**
     * 启用/禁用/删除商户账号
     * @return
     */
    boolean updateSysThirdUserStatus(EditStatusParam param);

    /**
     * 商户账号信息緩存redis
     * @return
     */
    void asyncUserToRedis(ThirdUser userInfo);


}
