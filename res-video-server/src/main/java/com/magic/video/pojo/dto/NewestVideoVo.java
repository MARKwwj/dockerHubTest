package com.magic.video.pojo.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewestVideoVo {
    @ApiModelProperty("当前页大小")
    private int pageSize;
    @ApiModelProperty("当前页码")
    private int pageNum;
}
