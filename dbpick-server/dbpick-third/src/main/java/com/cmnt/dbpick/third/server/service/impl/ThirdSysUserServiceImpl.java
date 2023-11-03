package com.cmnt.dbpick.third.server.service.impl;


import com.alibaba.nacos.common.utils.Md5Utils;
import com.cmnt.dbpick.common.constant.Constants;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.common.user.ThirdSysUserVO;
import com.cmnt.dbpick.common.utils.AccessAuthUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.common.utils.RandomSaltUtil;
import com.cmnt.dbpick.third.api.params.ThirdUserEditParam;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdSysUser;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;
import com.cmnt.dbpick.third.server.mongodb.repository.ThirdAccessKeyRepository;
import com.cmnt.dbpick.third.server.mongodb.repository.ThirdSysUserRepository;
import com.cmnt.dbpick.third.server.service.ThirdSysUserService;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserEditParam;
import com.cmnt.dbpick.third.api.params.SysThirdUserQueryParam;
import com.cmnt.dbpick.third.server.service.ThirdUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ThirdSysUserServiceImpl implements ThirdSysUserService {

    @Autowired
    private ThirdAccessKeyRepository thirdAccessKeyRepository;
    @Autowired
    private ThirdSysUserRepository thirdSysUserRepository;
    @Autowired
    private MongoPageHelper mongoPageHelper;

    @Autowired
    private AccessAuthUtil accessAuthUtil;

    @Autowired
    private ThirdUserService thirdUserService;


    /**
     * 根据商户id查询商户账号
     * @param thirdId
     */
    @Override
    public ThirdSysUser findSysThirdUserByThirdId(String thirdId){
        ThirdSysUser ThirdSysUser = thirdSysUserRepository.findTop1ByThirdIdAndDeleted(
                thirdId, Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));
        return ThirdSysUser;
    }

    /**
     * 获取商户账号列表
     * @param params
     * @return
     */
    @Override
    public PageResponse<ThirdSysUserVO> getSysThirdUserList(SysThirdUserQueryParam params) {
        Query query = new Query();
        if(StringUtils.isNotBlank(params.getThirdId())){
            query.addCriteria(Criteria.where("thirdId").regex(params.getThirdId()));
        }
        if(StringUtils.isNotBlank(params.getThirdName())){
            query.addCriteria(Criteria.where("thirdName").regex(params.getThirdName()));
        }
        if(StringUtils.isNotBlank(params.getStatus())){
            query.addCriteria(Criteria.where("status").is(params.getStatus()));
        }
        PageResponse<ThirdSysUser> data = mongoPageHelper.pageQuery(query, ThirdSysUser.class, params);
        PageResponse<ThirdSysUserVO> response = JacksonUtils.toBean(JacksonUtils.toJson(data), PageResponse.class);
        return response;
    }


    /**
     * 生成商户账号
     * @return
     */
    @Override
    public ThirdSysUserVO createSysThirdUser(SysThirdUserEditParam param){
        ThirdSysUser thirdSysUser = findSysThirdUserByThirdId(param.getThirdId());
        if(Objects.nonNull(thirdSysUser)){
            throw new BizException(ResponseCode.THIRD_USER_EXIST);
        }
        log.info("生成商户账号,param={}",param);
        thirdSysUser = new ThirdSysUser();
        FastBeanUtils.copy(param, thirdSysUser);
        String salt = getSalt();
        thirdSysUser.setSalt(salt);
        String password = Md5Utils.getMD5(param.getPassword() + salt, "utf-8");
        thirdSysUser.setPassword(password);
        thirdSysUser.setStatus(AbleStatusEnum.ENABLE.getValue());
        thirdSysUser.initSave(param.getCreateUser());
        ThirdUserEditParam userParam = ThirdUserEditParam.builder().thirdId(thirdSysUser.getThirdId())
                .userId(thirdSysUser.getThirdId()).userName(thirdSysUser.getThirdName())
                .password(thirdSysUser.getPassword()).userAvatar(thirdSysUser.getThirdAvatar())
                .status(thirdSysUser.getStatus()).createUser(thirdSysUser.getCreateUser()).build();
        thirdUserService.createThirdUser(userParam);
        thirdSysUserRepository.save(thirdSysUser);
        ThirdSysUserVO userVO = new ThirdSysUserVO();
        FastBeanUtils.copy(thirdSysUser, userVO);
        accessAuthUtil.setSysThirdUserInfo(userVO);
        return userVO;
    }

    public String getSalt(){
        String salt = RandomSaltUtil.generateRandomString(Constants.SALT_LENGTH);
        ThirdSysUser bySaltAndAndDeleted = thirdSysUserRepository.findBySaltAndAndDeleted(salt, Boolean.FALSE);
        if (Objects.nonNull(bySaltAndAndDeleted)){
            getSalt();
        }
        return salt;
    }

    /**
     * 商户账号
     */
    @Override
    public ThirdSysUserVO editSysThirdUser(SysThirdUserEditParam param) {
        log.info("更新直播房间 params={}", param);
        Optional<ThirdSysUser> optional = thirdSysUserRepository.findById(param.getId());
        if (!optional.isPresent()) {
            throw new BizException(ResponseCode.THIRD_USER_NOT_EXIST);
        }
        ThirdSysUser user = optional.get();
        if(StringUtils.isNotBlank(param.getThirdName())){
            user.setThirdName(param.getThirdName());
        }
        if(StringUtils.isNotBlank(param.getPassword())){
            user.setPassword(param.getPassword());
        }
        if(StringUtils.isNotBlank(param.getThirdAvatar())){
            user.setThirdAvatar(param.getThirdAvatar());
        }
        user.initUpdate(param.getCreateUser());
        thirdSysUserRepository.save(user);

        ThirdUserEditParam userParam = ThirdUserEditParam.builder()
                .thirdId(user.getThirdId()).userId(user.getThirdId())
                .userName(user.getThirdName()).password(user.getPassword()).userAvatar(user.getThirdAvatar())
                .createUser(user.getCreateUser()).build();
        thirdUserService.editThirdUser(userParam);

        ThirdSysUserVO userVO = new ThirdSysUserVO();
        FastBeanUtils.copy(user, userVO);
        accessAuthUtil.setSysThirdUserInfo(userVO);
        return userVO;
    }


    /**
     * 启用/禁用/删除商户账号
     * id    db id
     * type  操作类型: disable-禁用,enable-启动,delete-删除"
     * operator 操作员
     * @return
     */
    @Override
    public boolean updateSysThirdUserStatus(EditStatusParam param) {
        log.info("启用/禁用/删除商户账号,参数 param={}",param);
        Optional<ThirdSysUser> data = thirdSysUserRepository.findById(param.getId());
        if(!data.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_USER);
        }
        ThirdSysUser user = data.get();
        user.initUpdate(param.getCreateUser());
        ThirdSysUserVO userVO = new ThirdSysUserVO();
        FastBeanUtils.copy(user, userVO);
        AbleStatusEnum ableStatusEnum = AbleStatusEnum.getByValue(param.getType());
        switch (ableStatusEnum) {
            case DELETE:
                user.setDeleted(Boolean.TRUE);
                user.setStatus(AbleStatusEnum.DISABLE.getValue());
                accessAuthUtil.delSysThirdUserInfo(user.getThirdId());
                param.setId(user.getThirdId());
                thirdUserService.disableUserByThirdId(param);
                break;
            case DISABLE:
                log.info("禁用商户用户账号，param={}",param);
                user.setStatus(AbleStatusEnum.DISABLE.getValue());
                accessAuthUtil.setSysThirdUserInfo(userVO);
                //todo 禁用ak sk
                break;
            case ENABLE:
                user.setStatus(AbleStatusEnum.ENABLE.getValue());
                accessAuthUtil.setSysThirdUserInfo(userVO);
                thirdUserService.disableUserByThirdId(param);
                break;
            default:
                throw new BizException("操作类型错误!");
        }
        param.setId(user.getThirdId());
        thirdUserService.disableUserByThirdId(param);
        thirdSysUserRepository.save(user);
        return Boolean.TRUE;
    }



    /**
     * 商户账号信息緩存redis
     * @return
     */
    @Async
    @Override
    public void asyncUserToRedis(ThirdUser userInfo) {
        String thirdId = userInfo.getId();
        log.info("商户账号信息緩存redis--thirdId:{}",thirdId);
        ThirdAccessKeyVO akInfo = accessAuthUtil.getThirdAccessKeyInfo(thirdId);
        if(Objects.isNull(akInfo)){
            ThirdAccessKeyVO vo = thirdAccessKeyRepository.findTop1ByThirdIdAndDeleted(
                    thirdId, Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));
            log.info("商户账号信息緩存redis--findTop1ByThirdIdAndDeleted vo:{}",vo);
            accessAuthUtil.setThirdAccessKeyInfo(vo);
        }

    }


}
