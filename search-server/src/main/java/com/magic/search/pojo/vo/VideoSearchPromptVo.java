package com.magic.search.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class VideoSearchPromptVo {
    @ApiModelProperty("搜索关键词")
    private String keywords;
    @ApiModelProperty("当前页大小")
    private int pageSize;
    @ApiModelProperty("当前页码数")
    private int pageNum;
}
