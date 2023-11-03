package com.cmnt.dbpick.common.utils;

import java.util.regex.Pattern;

/**
 * 校验工具
 */
public class ValidatorUtil {

    /**
     * 校验手机号
     */
    public static boolean isMobile(String phone) {
        if (phone.contains(":")){
            phone = phone.split(":")[1];
        }
        String regex = "^(1[3-9])\\d{9}$";
        //String regex = "^(\\d{2}:)+(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[0-9])\\d{8}$";
        boolean isMobile = Pattern.compile(regex).matcher(phone).matches();
        return isMobile;
    }

    /**
     * 是否是图片
     * @param imgName
     * @return 是图片true,否则false
     */
    public static boolean isImage(String imgName){
        String[] extensions = {"jpg","jpeg","bmp","gif","png"};
        for (String extension : extensions){
            if (extension.equalsIgnoreCase(imgName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 是否是邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
        String regex = "^[A-Za-z\\d]+([-_.][A-Za-z\\d]+)*@([A-Za-z\\d]+[-.])+[A-Za-z\\d]{2,4}$";
        return Pattern.compile(regex).matcher(email).matches();
    }

    /**
     * 用户名只能是英文和数字
     * @param userName
     * @return
     */
    public static boolean isUserName(String userName){
        String regex = "^[0-9a-zA_Z\\-]+$";
        return Pattern.compile(regex).matcher(userName).matches();
    }


}
