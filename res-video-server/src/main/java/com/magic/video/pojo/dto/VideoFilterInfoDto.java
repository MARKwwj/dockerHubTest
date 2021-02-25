package com.magic.video.pojo.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class VideoFilterInfoDto {

    private Integer videoId;
    private String videoTitle;
    private String videoCover;
    private Integer videoDuration;
    private List<JSONObject> videoTagsList;
    private Integer videoMachineId;
    private Integer videoPayType;
    private Integer videoPayCoin;
    private Double videoScore;
    private Long videoPlayCount;
    private String videoSerialNum;

}
