package com.magic.search.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoSearchVo {
    @ApiModelProperty("搜索关键词")
    private String keywords;
    @ApiModelProperty("请求页大小")
    private int pageSize;
    @ApiModelProperty("请求当前页页码")
    private int pageNum;
    @ApiModelProperty("合集ID")
    private Integer categoryId;
}
