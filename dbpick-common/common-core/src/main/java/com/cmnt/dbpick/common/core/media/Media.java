package com.cmnt.dbpick.common.core.media;

import com.cmnt.dbpick.common.core.enums.MediaType;

/**
 * @author shusong.liang
 * @version 1.0.0
 * @Description 描述
 * @date 2022-09-18 14:19
 */
public class Media {
    private String source;

    private MediaType type;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public MediaType getType() {
        return type;
    }

    public void setType(MediaType type) {
        this.type = type;
    }

    public Media(String source, MediaType type) {
        this.source = source;
        this.type = type;
    }

}
