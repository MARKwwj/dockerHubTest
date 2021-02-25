package com.magic.video.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class VideoFilterParamsVo {

    @ApiModelProperty("根据排序类型筛选: 综合排序-0|最新更新-1|最多播放-2|最好评-3|最多收藏-4")
    private Integer orderBy;
    @ApiModelProperty("根据分类筛选: 由后端返回classifyId和classifyName")
    @NotNull
    private Integer classifyId;
    @ApiModelProperty("根据时长筛选: 全部时长-0|0至30分钟-1|30至60分钟-2|60至120分钟-3|120分钟以上-4")
    private Integer duration;
    @ApiModelProperty("根据付费类型筛选: 全部付费类型-0|免费-1|金币/VIP-2")
    private Integer videoPayType;
    @NotNull
    @ApiModelProperty("合集ID")
    private Integer categoryId;
    @NotNull
    @Min(1)
    @ApiModelProperty("分页大小")
    private Integer pageSize;
    @NotNull
    @Min(1)
    @ApiModelProperty("当前页码")
    private Integer pageNum;
}
