package com.cmnt.dbpick.common.utils;


public class UserInfoUtils {


    /*public static String concatUserRoom(String userId,String roomNo){
        return String.format("%s_%s",userId,roomNo);
    }

    public static String[] splitUserRoom(String userRoomStr) {
        return userRoomStr.split("_");
    }

    public static String getUserIdByUserRoom(String userRoomStr) {
        return splitUserRoom(userRoomStr)[0];
    }

    public static String getRoomNoByUserRoom(String userRoomStr) {
        return splitUserRoom(userRoomStr)[1];
    }*/


    public static String concatUserRoomRole(String userId,String roomNo, String userRole){
        return String.format("%s_%s_%s",userId,roomNo,userRole);
    }

    public static String[] splitUserRoomRole(String userRoomRoleStr) {
        return userRoomRoleStr.split("_");
    }

    public static String getUserIdByUserRoomRole(String userRoomRoleStr) {
        return splitUserRoomRole(userRoomRoleStr)[0];
    }


}
