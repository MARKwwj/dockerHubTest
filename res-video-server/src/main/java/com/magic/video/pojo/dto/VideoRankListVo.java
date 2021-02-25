package com.magic.video.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VideoRankListVo {

    @NotNull
    @ApiModelProperty("排行榜类型|1-观影榜|2-最新榜|3-付费榜|4-下载榜|5-收藏榜|6-热搜榜")
    private int rankType;
    @NotNull
    @ApiModelProperty("排行榜日期类型|1-每日|2-每周|3-每月")
    private int rankDateType;

}
