package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserLoginVo {
    @ApiModelProperty("机器码")
    private String machineCode;
    @ApiModelProperty("AppId")
    private Integer appId;
}
