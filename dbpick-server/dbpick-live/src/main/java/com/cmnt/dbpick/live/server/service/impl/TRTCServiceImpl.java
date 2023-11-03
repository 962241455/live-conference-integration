package com.cmnt.dbpick.live.server.service.impl;

import com.cmnt.dbpick.common.config.TencentConfig;
import com.cmnt.dbpick.common.utils.TxCloudUtil;
import com.cmnt.dbpick.live.api.vo.TxConfigVo;
import com.cmnt.dbpick.live.server.service.TRTCService;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sqlserver.v20180328.models.DescribeZonesRequest;
import com.tencentcloudapi.trtc.v20190722.TrtcClient;
import com.tencentcloudapi.trtc.v20190722.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TRTCServiceImpl implements TRTCService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    //请求腾讯im的地址中usersig签名生成必须是管理员userId
    private static final String APP_MANAGER = "administrator";

    @Autowired
    private TxCloudUtil txCloudUtil;


    @Override
    public TxConfigVo getTencentConfig() {
        TxConfigVo vo = TxConfigVo.builder()
                .userSig(txCloudUtil.getTxCloudUserSig(APP_MANAGER))
                .adminId(APP_MANAGER)
                .sdkappid(TencentConfig.TX_CLOUD_SDK_APPID)
                .pushUrl(TencentConfig.TX_CLOUD_PUSH_URL)
                .playUrl(TencentConfig.TX_CLOUD_PLAYURL)
                .build();
        return vo;
    }

    /**
     * 接口说明：把房间所有用户从房间踢出，解散房间。支持所有平台，Android、iOS、Windows 和 macOS 需升级到 TRTC SDK 6.6及以上版本。
     */
    @Override
    public void dissolveRoom(String uid, Long roomId) {
        logger.debug("解散直播房间uid={}", uid);
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(TencentConfig.TX_CLOUD_SECRETID, TencentConfig.TX_CLOUD_SECRETKEY);

            // 实例化要请求产品(以cvm为例)的client对象
            TrtcClient client = new TrtcClient(cred, TencentConfig.REGION);
            // 实例化一个请求对象
            DismissRoomRequest req = new DismissRoomRequest();
            req.setSdkAppId(TencentConfig.TX_CLOUD_SDK_APPID);
            req.setRoomId(roomId);

            logger.debug("解散直播房间请求req={}", DescribeZonesRequest.toJsonString(req));
            // 通过client对象调用想要访问的接口，需要传入请求对象
            DismissRoomResponse resp = client.DismissRoom(req);
            logger.debug("解散直播房间响应resp={}", DescribeZonesRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            logger.error("解散直播房间失败", e);
        }

    }


    @Override
    public void kickOutUser(Long roomId, String... uids) {
        logger.debug("直播房间踢人uid={}", uids);
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey
            Credential cred = new Credential(TencentConfig.TX_CLOUD_SECRETID, TencentConfig.TX_CLOUD_SECRETKEY);

            // 实例化要请求产品(以cvm为例)的client对象
            TrtcClient client = new TrtcClient(cred, TencentConfig.REGION);
            // 实例化一个请求对象
            RemoveUserByStrRoomIdRequest req = new RemoveUserByStrRoomIdRequest();
            req.setUserIds(uids);
            req.setRoomId(String.valueOf(roomId));
            req.setSdkAppId(TencentConfig.TX_CLOUD_SDK_APPID);
            logger.debug("直播房间踢人请求req={}", RemoveUserByStrRoomIdResponse.toJsonString(req));
            // 通过client对象调用想要访问的接口，需要传入请求对象
            RemoveUserByStrRoomIdResponse  resp = client.RemoveUserByStrRoomId(req);
            logger.debug("直播房间踢人响应resp={}", RemoveUserByStrRoomIdResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            logger.error("直播房间踢人失败", e);
        }
    }

}
