package com.magic.video.pojo.dto;

import lombok.Data;

@Data
public class NewestVideoDto {
    private Integer videoId;
    private String videoTitle;
    private Integer videoDuration;
    private Integer videoMachineId;
    private Integer videoPayType;
    private Integer videoPayCoin;
    private Double videoScore;
}
