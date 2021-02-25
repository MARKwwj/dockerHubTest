package com.magic.longvideo.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class VideoInfoDto {

    private Date watchDate;
    private String videoScore;
    private String hms;
    private Integer videoMachineId;
    private Integer videoPayType;
    private Integer videoPayCoin;
    private Integer videoDuration;
    private int watchProgress;
    private Long videoId;
    private String videoTitle;

}
