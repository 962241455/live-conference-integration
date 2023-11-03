package com.cmnt.dbpick.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class EnvironmentUtil {

    public static final String ENV_PROD = "prod";

    /**
     * 判断当前环境
     */
    public static String getActiveProfile() {
        return SpringBeanFactoryUtils.getApplicationContext().getEnvironment().getActiveProfiles()[0];
    }


    public static Boolean isProd() {
        String activeProfile = getActiveProfile();
        log.info("activeProfile is {}",activeProfile);
        if(StringUtils.isNotBlank(activeProfile) && activeProfile.contains(ENV_PROD)){
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
