package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.config.TencentConfig;
import com.cmnt.dbpick.common.constant.RedisKey;
import com.cmnt.dbpick.common.model.ThirdAccessKeyInfo;
import com.cmnt.dbpick.common.user.RoomConfigVO;
import com.cmnt.dbpick.common.utils.AccessAuthUtil;
import com.cmnt.dbpick.common.utils.DateUtil;
import com.cmnt.dbpick.common.utils.FastBeanUtils;
import com.cmnt.dbpick.common.utils.RedisUtils;
import com.cmnt.dbpick.live.api.model.StreamingRoomInfo;
import com.cmnt.dbpick.live.api.params.RoomConfigEditParam;
import com.cmnt.dbpick.live.api.params.StreamingRoomParams;
import com.cmnt.dbpick.live.api.vo.TxCosConfigVo;
import com.cmnt.dbpick.live.server.mongodb.document.StreamingRoomConfig;
import com.cmnt.dbpick.live.server.mongodb.repository.StreamingRoomConfigRepository;
import com.cmnt.dbpick.live.server.service.RoomConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;


@Slf4j
@Service
public class RoomConfigServiceImpl implements RoomConfigService {

    @Autowired
    private StreamingRoomConfigRepository roomConfigRepository;

    @Autowired
    private AccessAuthUtil accessAuthUtil;
    @Autowired
    private RedisUtils redisUtils;


    /**
     * 查询直播间默认配置信息
     * @param param
     * @return
     */
    @Override
    public RoomConfigVO info(ThirdAccessKeyInfo param) {
        StreamingRoomConfig roomConfig =
                roomConfigRepository.findTop1ByThirdIdOrderByCreateDateTimeDesc(param.getThirdId());
        RoomConfigVO vo = new RoomConfigVO();
        if(Objects.nonNull(roomConfig)){
            FastBeanUtils.copy(roomConfig, vo);
        }
        return vo;
    }

    /**
     * 编辑直播间默认配置
     */
    @Override
    public RoomConfigVO edit(RoomConfigEditParam param) {
        String thirdAK = accessAuthUtil.getThirdAK(param.getThirdId());

        log.info("编辑直播间默认配置 param={}", param);
        StreamingRoomConfig config = new StreamingRoomConfig();
        FastBeanUtils.copy(param, config);
        if(StringUtils.isBlank(param.getId())){
            config.setAk(thirdAK);
            config.initSave(param.getCreateUser());
        }
        config.initUpdate(param.getCreateUser());
        StreamingRoomConfig save = roomConfigRepository.save(config);
        RoomConfigVO vo = new RoomConfigVO();
        FastBeanUtils.copy(save, vo);
        accessAuthUtil.setRoomConfigInfo(vo, param.getThirdId());
        return vo;
    }


    /** 新建房间时处理直播间默认设置 */
    @Override
    public StreamingRoomInfo dealDefaultRoomConfig(StreamingRoomInfo info, StreamingRoomParams param){
        String thirdId = param.getThirdId();
        String paramTitle = param.getTitle();
        RoomConfigVO roomConfig = null;
        if(StringUtils.isBlank(paramTitle)){
            roomConfig = accessAuthUtil.getRoomConfigInfo(thirdId);
            info.setTitle(roomConfig.getTitle());
        }
        String paramBgImg = param.getBgImg();
        if(StringUtils.isBlank(paramBgImg)){
            roomConfig = Objects.isNull(roomConfig) ? accessAuthUtil.getRoomConfigInfo(thirdId):roomConfig;
            info.setBgImg(roomConfig.getBgImg());
        }
        String logoCoverStr = param.getLogoCover();
        if(StringUtils.isBlank(logoCoverStr)){
            roomConfig = Objects.isNull(roomConfig) ? accessAuthUtil.getRoomConfigInfo(thirdId):roomConfig;
            logoCoverStr = roomConfig.getLogoCover();
        }
        info.setLogoCover(new HashSet<>(Arrays.asList(logoCoverStr.split(","))));
        return info;
    }


    //获取文件最大size (单位 GB)
    @Override
    public Integer getFileMaxSize(String fileType){
        Integer defaultSize = 3;
        String redisKey = RedisKey.UPLOAD_FILE_MAX_SIZE_GB;
        switch (fileType){
            case "image":
                defaultSize = 2;
                redisKey = RedisKey.UPLOAD_FILE_MAX_SIZE_MB;
                break;
            case "video":
            default:
                break;
        }
        Object object = redisUtils.get(redisKey);
        if (Objects.isNull(object)) {
            redisUtils.set(redisKey, defaultSize);
            return defaultSize;
        }
        return (int)object;
    }

    @Override
    public TxCosConfigVo getCosInfo(String createBy) {
        return TxCosConfigVo.builder()
                .secretId(TencentConfig.TX_CLOUD_SECRETID).secretKey(TencentConfig.TX_CLOUD_SECRETKEY)
                .region(TencentConfig.REGION).bucketName(TencentConfig.TX_COS_BUCKET_NAME)
                //     /video/test/createBy/yyyy-MM-dd
                .foldersPrefix("/"+TencentConfig.TX_COS_FOLDERS_PREFIX+createBy+"/"+DateUtil.nowDateTime(DateUtil.Y_M_D)+"/")
                .accessUrl(TencentConfig.TX_COS_ACCESS_URL)
                .build();
    }
}
