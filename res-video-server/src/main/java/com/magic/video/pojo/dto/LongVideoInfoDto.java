package com.magic.video.pojo.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class LongVideoInfoDto {
    private String videoTitle;
    private Integer videoId;
    private String videoIntro;
    private String videoSerialNum;
    private Integer videoDuration;
    private String hms;
    private String videoUrl;
    private String videoPlayCount;
    private Long videoPraiseCount;
    private Long videoFavoriteCount;
    private Long videoByteSize;
    private Integer videoPayType;
    private int videoPayCoin;
    private Double videoScore;
    private boolean isPraise;
    private boolean isFavorite;
    private List<JSONObject> videoTags;
    private java.util.Date videoFreeExpires;
    private java.util.Date videoUpdateTime;
    private Integer videoMachineId;
    private int lastWatchedTime;
    private int videoTryWatchSecond = 30;
}
