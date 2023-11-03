package com.cmnt.dbpick.third.server.service.impl;

import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.jwt.JwtToken;
import com.cmnt.dbpick.common.jwt.pojo.TokenInfo;
import com.cmnt.dbpick.common.utils.BeanMapUtils;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.api.params.ThirdUserEditParam;
import com.cmnt.dbpick.third.api.vo.ThirdUserLoginVO;

import com.cmnt.dbpick.third.server.mongodb.document.ThirdUser;
import com.cmnt.dbpick.third.server.mongodb.repository.ThirdUserRepository;
import com.cmnt.dbpick.third.server.service.ThirdUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class ThirdUserServiceImpl implements ThirdUserService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ThirdUserRepository thirdUserRepository;

    /**
     * 生成商户子账号
     * @param param
     */
    @Override
    public ThirdUserLoginVO createThirdUser(ThirdUserEditParam param) {
        ThirdUser thirdUser = thirdUserRepository.findTop1ByThirdIdAndUserIdAndDeleted(
                param.getThirdId(),param.getUserId(),Boolean.FALSE,Sort.by(Sort.Direction.DESC, "createDateTime"));
        if(Objects.nonNull(thirdUser)){
            throw new BizException(ResponseCode.THIRD_USER_EXIST);
        }
        log.info("生成商户子账号,param={}",param);
        thirdUser = new ThirdUser();
        FastBeanUtils.copy(param, thirdUser);
        thirdUser.setStatus(AbleStatusEnum.ENABLE.getValue());
        thirdUser.initSave(param.getCreateUser());
        thirdUserRepository.save(thirdUser);
        ThirdUserLoginVO vo = new ThirdUserLoginVO();
        FastBeanUtils.copy(thirdUser, vo);
        return vo;
    }


    /**
     * 更新商户子账号
     */
    @Override
    public ThirdUserLoginVO editThirdUser(ThirdUserEditParam param) {
        log.info("更新商户子账号 param={}", param);
        ThirdUserLoginVO vo = new ThirdUserLoginVO();
        ThirdUser user = findThirdUserByUserId(param.getUserId());
        if (Objects.isNull(user)) {
            return vo;
        }
        if(StringUtils.isNotBlank(param.getUserName())){
            user.setUserName(param.getUserName());
        }
        if(StringUtils.isNotBlank(param.getPassword())){
            user.setPassword(param.getPassword());
        }
        if(StringUtils.isNotBlank(param.getUserAvatar())){
            user.setUserAvatar(param.getUserAvatar());
        }
        user.initUpdate(param.getCreateUser());
        thirdUserRepository.save(user);
        FastBeanUtils.copy(user, vo);
        return vo;
    }

    /**
     * 根据用户id查询登录信息
     * @param userId
     * @return
     */
    @Override
    public ThirdUser findThirdUserByUserId(String userId) {
        ThirdUser thirdUser = thirdUserRepository.findTop1ByUserIdAndDeleted(
                userId, Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));
        return thirdUser;
    }

    @Override
    public ThirdUser createLoginToken(ThirdUserLoginVO vo) throws Exception{
        ThirdUser user = findThirdUserByUserId(vo.getUsername());
        //todo 重写获取token，redis
        if(Objects.isNull(user)){
            throw new BizException("商户账号不存在！");
        }
        if (!StringUtils.equals(vo.getPassword(), user.getPassword())) {
            throw new BizException("密码错误");
        }
        if (StringUtils.equals(user.getStatus(), AbleStatusEnum.DISABLE.getValue())) {
            throw new BizException("账号已经禁用！");
        }
        return  user;
    }

    /**
     * 根据商户账号状态，更新子账号状态
     * @return
     */
    @Override
    public boolean disableUserByThirdId(EditStatusParam param) {
        log.info("禁用第三方用户账号，param={}",param);
        String thirdId = param.getId();
        AbleStatusEnum ableStatusEnum = AbleStatusEnum.getByValue(param.getType());
        Update update = new Update();
        switch (ableStatusEnum) {
            case DELETE:
                update = new Update().set("status", AbleStatusEnum.DISABLE.getValue())
                .set("deleted", Boolean.TRUE);
                break;
            case DISABLE:
                update = new Update().set("status", AbleStatusEnum.DISABLE.getValue());
                break;
            case ENABLE:
                update = new Update().set("status", AbleStatusEnum.ENABLE.getValue());
                break;
        }
        mongoTemplate.updateMulti(new Query(Criteria.where("thirdId").is(thirdId)), update, ThirdUser.class);
        return Boolean.TRUE;
    }


}
