package com.magic.video.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RankListVideoInfoDto {

    @ApiModelProperty("视频Id")
    private Long videoId;
    @ApiModelProperty("视频时长")
    private Integer videoDuration;
    @ApiModelProperty("视频评分")
    private Integer videoScore;
    @ApiModelProperty("视频名称")
    private String videoTitle;
    @ApiModelProperty("机器码")
    private Integer videoMachineId;
    @ApiModelProperty("付费金币数")
    private Integer videoPayCoin;
    @ApiModelProperty("付费类型")
    private Integer videoPayType;
    @ApiModelProperty("排行榜分数")
    private Integer rankScore;
    @ApiModelProperty("排行榜排名")
    private Integer rankIndex;
    @ApiModelProperty("排名浮动情况|0-上升|1-保持|2-下降")
    private Integer status;
}
