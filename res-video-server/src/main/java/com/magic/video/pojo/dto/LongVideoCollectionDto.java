package com.magic.video.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LongVideoCollectionDto {

    private Integer videoId;
    private String videoTitle;
    private Integer videoDuration;
    private String hms;
    private Integer videoMachineId;
    private Integer videoPayType;
    private Integer videoPayCoin;
    private Double videoScore;

}
