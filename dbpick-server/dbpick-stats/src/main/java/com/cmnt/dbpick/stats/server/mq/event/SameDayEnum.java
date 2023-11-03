package com.cmnt.dbpick.stats.server.mq.event;

import com.cmnt.dbpick.common.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum SameDayEnum {
    SAME_DAY("same_day","同一天"),
    DIFF_DAY("diff_day","非同一天"),
    ;


    private String eventType;
    private String handler;


    public static SameDayEnum getEnum(String type){
        for (SameDayEnum value : SameDayEnum.values()) {
            if (type==value.eventType){
                return value;
            }
        }
        return null;
    }


    public static SameDayEnum getEnumByDate(String day1, String day2){
        if(DateUtils.isSameDay(DateUtil.str2Date(day1, DateUtil.Y_M_D_HMS),
                DateUtil.str2Date(day2, DateUtil.Y_M_D_HMS))){
            return SAME_DAY;
        }
        return DIFF_DAY;
    }

}
