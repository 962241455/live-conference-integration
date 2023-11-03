package com.cmnt.dbpick.third.server.service.impl;

import com.cmnt.dbpick.common.exception.BizException;
import com.cmnt.dbpick.common.page.MongoPageHelper;
import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.JacksonUtils;
import com.cmnt.dbpick.common.utils.StringUtils;
import com.cmnt.dbpick.third.api.params.MerchantPackageEditParam;
import com.cmnt.dbpick.third.api.params.MerchantPackageQueryParam;
import com.cmnt.dbpick.third.api.vo.MerchantPackageVo;
import com.cmnt.dbpick.third.api.vo.ThirdRouterVo;
import com.cmnt.dbpick.third.server.mongodb.document.ThirdMerchantPackage;
import com.cmnt.dbpick.third.server.mongodb.repository.ThirdMerchantPackageRepository;
import com.cmnt.dbpick.third.server.service.ThirdMerchantPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class ThirdMerchantPackageServiceImpl implements ThirdMerchantPackageService {

    @Autowired
    private ThirdMerchantPackageRepository thirdMerchantPackageRepository;

    @Autowired
    private MongoPageHelper mongoPageHelper;

    /**
     * 获取商户套餐列表
     * @param param
     * @return
     */
    @Override
    public PageResponse<MerchantPackageVo> getMerchantPackageList(MerchantPackageQueryParam param) {
        Query query = new Query();
        if (StringUtils.isNotBlank(param.getName())){
            query.addCriteria(Criteria.where("name").regex(param.getName()));
        }
        query.with(Sort.by(Sort.Direction.DESC, "createDateTime"));
        PageResponse<ThirdMerchantPackage> response = mongoPageHelper.pageQuery(query, ThirdMerchantPackage.class, param);
        PageResponse<MerchantPackageVo> pageResponse = JacksonUtils.toBean(JacksonUtils.toJson(response), PageResponse.class);
        return pageResponse;
    }

    /**
     * 新增商户套餐
     * @param param
     * @return
     */
    @Override
    public MerchantPackageVo createMerchantPackage(MerchantPackageEditParam param) {
        ThirdMerchantPackage byNameAndDeleted = thirdMerchantPackageRepository.findByNameAndDeleted(param.getName(), Boolean.FALSE);
        if (Objects.nonNull(byNameAndDeleted)){
            throw new BizException("已存在该名称的套餐!");
        }
        ThirdMerchantPackage thirdMerchantPackage = new ThirdMerchantPackage();
        FastBeanUtils.copy(param, thirdMerchantPackage);
        thirdMerchantPackage.initSave("");
        thirdMerchantPackageRepository.save(thirdMerchantPackage);
        MerchantPackageVo merchantPackageVo = new MerchantPackageVo();
        FastBeanUtils.copy(thirdMerchantPackage, merchantPackageVo);
        return merchantPackageVo;
    }

    /**
     * 修改商户套餐
     * @param param
     * @return
     */
    @Override
    public MerchantPackageVo updateMerchantPackage(MerchantPackageEditParam param) {
        Optional<ThirdMerchantPackage> byId = thirdMerchantPackageRepository.findById(param.getId());
        if (!byId.isPresent()){
            throw new BizException("未找到该套餐");
        }
        ThirdMerchantPackage thirdMerchantPackage = byId.get();
        ThirdMerchantPackage byNameAndDeleted = thirdMerchantPackageRepository.findByNameAndDeleted(param.getName(), Boolean.FALSE);
        if (Objects.nonNull(byNameAndDeleted) && !StringUtils.equals(thirdMerchantPackage.getId(), byNameAndDeleted.getId())){
            throw new BizException("已存在该名称的套餐!");
        }
        thirdMerchantPackage.setName(param.getName());
        thirdMerchantPackage.setBroadcastingRoom(param.getBroadcastingRoom());
        thirdMerchantPackage.setTemporaryRoom(param.getTemporaryRoom());
        thirdMerchantPackage.setStorageSpace(param.getStorageSpace());
        thirdMerchantPackage.setRates(param.getRates());
        thirdMerchantPackage.setTranscodingDuration(param.getTranscodingDuration());
        thirdMerchantPackage.setSupportingFunction(param.getSupportingFunction());
        thirdMerchantPackage.setViewableAudience(param.getViewableAudience());
        thirdMerchantPackage.setGetSimNum(param.getGetSimNum());
        thirdMerchantPackage.setPackagePrice(param.getPackagePrice());
        thirdMerchantPackage.initUpdate("");
        thirdMerchantPackageRepository.save(thirdMerchantPackage);
        MerchantPackageVo merchantPackageVo = new MerchantPackageVo();
        FastBeanUtils.copy(thirdMerchantPackage, merchantPackageVo);
        return null;
    }

    /**
     * 删除商户套餐
     * @param id
     */
    @Override
    public void delMerchantPackage(String id) {
        Optional<ThirdMerchantPackage> byId = thirdMerchantPackageRepository.findById(id);
        if (!byId.isPresent()){
            throw new BizException("未找到该套餐");
        }
        ThirdMerchantPackage thirdMerchantPackage = byId.get();
        thirdMerchantPackage.initDel();
        thirdMerchantPackageRepository.save(thirdMerchantPackage);
    }
}
