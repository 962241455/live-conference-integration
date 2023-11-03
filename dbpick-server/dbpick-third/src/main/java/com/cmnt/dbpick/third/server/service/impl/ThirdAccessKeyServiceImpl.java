package com.cmnt.dbpick.third.server.service.impl;


import com.cmnt.dbpick.common.constant.Constants;
import com.cmnt.dbpick.common.enums.AbleStatusEnum;
import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.exception.ResponseCode;
import com.cmnt.dbpick.common.user.ThirdAccessKeyVO;
import com.cmnt.dbpick.common.utils.AccessAuthUtil;
import com.cmnt.dbpick.common.utils.AccessKeyUtils;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.third.api.params.EditStatusParam;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdAccessKey;
import com.cmnt.dbpick.third.server.mongodb.repository.ThirdAccessKeyRepository;
import com.cmnt.dbpick.third.server.service.ThirdAccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * 访问凭证
 */
@Slf4j
@Service
public class ThirdAccessKeyServiceImpl implements ThirdAccessKeyService {


    @Autowired
    private ThirdAccessKeyRepository thirdAccessKeyRepository;

    @Autowired
    private AccessAuthUtil accessAuthUtil;

    /**
     * 获取访问凭证
     * @param thirdId
     * @return
     */
    @Override
    public ThirdAccessKeyVO findAccessKeyByUser(String thirdId) {
        ThirdAccessKeyVO vo = thirdAccessKeyRepository.findTop1ByThirdIdAndDeleted(
                thirdId, Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));
        return vo;
    }

    /**
     * 生成访问凭证
     * @param userId
     * @return
     */
    @Override
    public ThirdAccessKeyVO createAccess(String thirdId, String userId){
        /*ThirdAccessKeyVO data = thirdAccessKeyRepository.findTop1ByThirdIdAndStatusAndDeleted(
                thirdId, AbleStatusEnum.ENABLE.getValue(), Boolean.FALSE, Sort.by(Sort.Direction.DESC, "createDateTime"));*/
        ThirdAccessKeyVO data = findAccessKeyByUser(thirdId);
        if(Objects.nonNull(data)){
            throw new BizException(ResponseCode.ACCESS_KEY_EXIST);
        }
        ThirdAccessKey accessKey = saveAccesseKey(thirdId, userId);
        ThirdAccessKeyVO vo = new ThirdAccessKeyVO();
        FastBeanUtils.copy(accessKey, vo);
        accessAuthUtil.setThirdAccessKeyInfo(vo);
        return vo;
    }

    private ThirdAccessKey saveAccesseKey(String thirdId, String userId){
        String accessKeyId = AccessKeyUtils.createAccessKey();
        String accessKeySecret = AccessKeyUtils.createAccessSecret();
        ThirdAccessKey accessKey = ThirdAccessKey.builder().thirdId(thirdId)
                .platform(Constants.CMNT_DEFAULT_PLATFORM).appName(Constants.CMNT_DEFAULT_APPNAME)
                .accessKeyId(accessKeyId).accessKeySecret(accessKeySecret)
                .expireTime(-1L).status(AbleStatusEnum.ENABLE.getValue())
                .build();
        accessKey.initSave(userId);
        thirdAccessKeyRepository.save(accessKey);
        return accessKey;
    }

    /**
     * 更新访问凭证
     * @return
     */
    @Override
    public ThirdAccessKeyVO updateAccess(EditStatusParam param) {
        Optional<ThirdAccessKey> data = thirdAccessKeyRepository.findById(param.getId());
        if(!data.isPresent()){
            throw new BizException(ResponseCode.NOT_FIND_ACCESS);
        }
        ThirdAccessKey accessKey = data.get();
        if(!StringUtils.equals(param.getCreateUser(), accessKey.getCreateUser())){
            throw new BizException("无权操作当前凭证!");
        }
        accessKey.initUpdate(param.getCreateUser());
        ThirdAccessKeyVO vo = new ThirdAccessKeyVO();
        FastBeanUtils.copy(accessKey, vo);
        AbleStatusEnum ableStatusEnum = AbleStatusEnum.getByValue(param.getType());
        switch (ableStatusEnum) {
            case DISABLE: //启用
                accessKey.setStatus(AbleStatusEnum.DISABLE.getValue());
                accessAuthUtil.setThirdAccessKeyInfo(vo);
                break;
            case ENABLE: //禁用
                accessKey.setStatus(AbleStatusEnum.ENABLE.getValue());
                accessAuthUtil.setThirdAccessKeyInfo(vo);
                break;
            case DELETE: //删除
                accessKey.setDeleted(Boolean.TRUE);
                accessKey.setStatus(AbleStatusEnum.DISABLE.getValue());
                accessAuthUtil.delThirdAccessKeyInfo(accessKey.getThirdId());
                break;
            default:
                throw new BizException("操作类型错误!");
        }
        thirdAccessKeyRepository.save(accessKey);
        return vo;
    }

    /**
     * 校验ak sk 是否可用
     * @param accessKeyId
     * @param accessKeySecret
     * @return
     */
    @Override
    public ThirdAccessKeyVO existsAccess(String accessKeyId, String accessKeySecret) {
        return thirdAccessKeyRepository.findTop1ByAccessKeyIdAndAccessKeySecretAndStatusAndDeletedOrderByCreateDateDesc(
                accessKeyId, accessKeySecret, AbleStatusEnum.ENABLE.getValue(), Boolean.FALSE);
    }

}
