package com.magic.search.pojo.dto;

import cn.hutool.json.JSONObject;
import lombok.Data;

import java.util.List;

@Data
public class SearchResultDto {
    private Integer videoId;
    private String videoTitle;
    private String videoCover;
    private Integer videoDuration;
    private String videoSerialNum;
    private List<JSONObject> videoTagsList;
    private Integer videoMachineId;
    private Integer videoPayType;
    private Integer videoPayCoin;
    private Double videoScore;
    private String videoPlayCount;
    private String hms;

}
