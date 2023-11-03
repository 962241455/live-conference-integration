/**
 * Demo class
 *
 * @author 28021
 * @date 2022/8/11
 */
package com.cmnt.dbpick.live.server.service;

import com.cmnt.dbpick.common.page.PageResponse;
import com.cmnt.dbpick.live.api.params.third.GmLiveClassIfyParams;
import com.cmnt.dbpick.live.api.vo.third.LiveClassifyVo;
import com.cmnt.dbpick.live.server.mongodb.document.LiveClassify;

/**
 * 房间分类管管理
 *
 * @author mr . wei
 * @date 2022/8/11
 */
public interface LiveClassifyService {

    boolean save(LiveClassify params) throws Exception;

    public LiveClassify getById(String id);

    public PageResponse<LiveClassifyVo> getLiveClassifyList(GmLiveClassIfyParams params) throws Exception;

    boolean isDeletel(String id);

    boolean updataLiveClassify(LiveClassify params) throws Exception;

}
