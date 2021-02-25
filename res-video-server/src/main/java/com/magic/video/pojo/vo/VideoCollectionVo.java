package com.magic.video.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoCollectionVo {
    @ApiModelProperty("当前页大小")
    private int pageSize;
    @ApiModelProperty("合集ID")
    private String collectionId;
    @ApiModelProperty("当前页码")
    private int pageNum;
}
