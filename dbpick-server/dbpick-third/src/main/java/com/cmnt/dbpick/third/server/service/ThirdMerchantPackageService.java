package com.cmnt.dbpick.third.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.third.api.params.MerchantPackageEditParam;
import com.cmnt.dbpick.third.api.params.MerchantPackageQueryParam;
import com.cmnt.dbpick.third.api.vo.MerchantPackageVo;

import java.util.List;

public interface ThirdMerchantPackageService {

    /**
     * 获取商户套餐列表
     * @param param
     * @return
     */
    PageResponse<MerchantPackageVo> getMerchantPackageList(MerchantPackageQueryParam param);
    /**
     * 新增商户套餐
     * @param param
     * @return
     */
    MerchantPackageVo createMerchantPackage(MerchantPackageEditParam param);

    /**
     * 修改商户套餐
     * @param param
     * @return
     */
    MerchantPackageVo updateMerchantPackage(MerchantPackageEditParam param);

    /**
     * 删除商户套餐
     * @param id
     */
    void delMerchantPackage(String id);

}
