package com.magic.longvideo.common.consts;

public enum UserVideoMapFieldName {

    HISTORY("HISTORY"),
    FAVORITE("FAVORITE"),
    PURCHASED("PURCHASED"),
    ;
    private final String text;
    UserVideoMapFieldName(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
}
