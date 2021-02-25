package com.magic.video.common.consts;

public enum VideoRankType {

    WATCH_RANK_LIST(1, "WATCH_RANK_LIST"),
    NEWEST_RANK_LIST(2, "NEWEST_RANK_LIST"),
    PAID_RANK_LIST(3, "PAID_RANK_LIST"),
    DOWNLOAD_RANK_LIST(4, "DOWNLOAD_RANK_LIST"),
    FAVOURITE_RANK_LIST(5, "FAVOURITE_RANK_LIST"),
    HOT_SEARCH_RANK_LIST(6, "HOT_SEARCH_RANK_LIST"),
    ;
    private final int key;
    private final String value;

    VideoRankType(final int key, final String value) {
        this.key = key;
        this.value = value;
    }


    public static String getValueByKey(int key) {
        for (VideoRankType videoRankType : VideoRankType.values()) {
            if (videoRankType.key == key) {
                return videoRankType.value;
            }
        }
        return null;
    }
}
