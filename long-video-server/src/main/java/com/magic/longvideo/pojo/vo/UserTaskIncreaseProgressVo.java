package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserTaskIncreaseProgressVo {
    @ApiModelProperty("任务模式")
    private Integer taskMode;
    @ApiModelProperty("任务类型")
    private Integer taskType;
    @ApiModelProperty("递增数值")
    private int increaseNum;
}
