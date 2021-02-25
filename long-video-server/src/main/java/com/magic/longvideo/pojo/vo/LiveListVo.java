package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LiveListVo {
    @ApiModelProperty("当前页大小")
    private int pageSize;
    @ApiModelProperty("当前页码")
    private int pageNum;
    @ApiModelProperty("AppId")
    private String appId;
}
