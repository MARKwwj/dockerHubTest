package com.magic.longvideo.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LongVideoTaskInfoDto {

    private Integer taskId;
    private String taskProgress;
    private String taskAwardContent;
    private int taskMode;
    private int taskState;
    private Integer taskType;
    private String taskLogo;
}
