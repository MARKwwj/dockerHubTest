package com.magic.video.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class VideoInfoDto {

    private Date watchDate;
    private int watchProgress;
    private Long videoId;
    private String videoTitle;

}
