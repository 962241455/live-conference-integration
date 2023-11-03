package com.cmnt.dbpick.common.utils;


/**
 * 直播间常量工具
 */
public class RoomCommonUtil {

    public static final String PLAYBACK_ROOM_ID = "_playback";


    public static String getPlaybackRoom(String roomNo){
        return String.format("%s%s",roomNo,PLAYBACK_ROOM_ID);
    }

    public static String getRoomWithPlayback(String playbackRoomNo){
        return playbackRoomNo.replace(PLAYBACK_ROOM_ID,"");
    }

}
