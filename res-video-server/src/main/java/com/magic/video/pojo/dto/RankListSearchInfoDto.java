package com.magic.video.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankListSearchInfoDto {

    @ApiModelProperty("搜索关键字")
    private String searchValue;
    @ApiModelProperty("排行榜分数")
    private Integer rankScore;
    @ApiModelProperty("排行榜排名")
    private Integer rankIndex;
    @ApiModelProperty("排名浮动情况|0-上升|1-保持|2-下降")
    private Integer status;
}
