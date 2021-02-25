package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class VideoInfoVo {
    @ApiModelProperty("观看日期|时间戳")
    private Date watchDate;
    @ApiModelProperty("观看进度|单位:视频总时长百分比")
    private Integer watchProgress;
    @ApiModelProperty("视频Id")
    private Long videoId;
    @ApiModelProperty("机器Id")
    private Integer videoMachineId;
    @ApiModelProperty("视频时长")
    private Integer videoDuration;
    @ApiModelProperty("时分秒")
    private String hms;
    @ApiModelProperty("视频评分")
    private Integer videoScore;
    @ApiModelProperty("视频名称")
    private String videoTitle;
    @ApiModelProperty("付费金币数")
    private Integer videoPayCoin;
    @ApiModelProperty("付费类型|0-免费|1-金币/VIP")
    private Integer videoPayType;
}
