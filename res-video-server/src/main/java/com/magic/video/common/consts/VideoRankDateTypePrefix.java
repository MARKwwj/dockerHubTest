package com.magic.video.common.consts;

public enum VideoRankDateTypePrefix {

    DAILY(1, "DAILY_"),
    WEEKLY(2, "WEEKLY_"),
    MONTHLY(3, "MONTHLY_"),
    ;
    private final int key;
    private final String value;

    VideoRankDateTypePrefix(final int key, final String value) {
        this.key = key;
        this.value = value;
    }


    public static String getValueByKey(int key) {
        for (VideoRankDateTypePrefix videoRankType : VideoRankDateTypePrefix.values()) {
            if (videoRankType.key == key) {
                return videoRankType.value;
            }
        }
        return null;
    }
}
