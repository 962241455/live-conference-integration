package com.cmnt.dbpick.common.utils;

import com.cmnt.dbpick.common.constant.Constants;

import java.security.SecureRandom;

public class RandomSaltUtil {

    private static SecureRandom random = new SecureRandom();

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(Constants.SALT_VALUE.length());
            char randomChar = Constants.SALT_VALUE.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}