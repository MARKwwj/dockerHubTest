package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserJoinVo {
    @ApiModelProperty("机器码")
    private String machineCode;
    @ApiModelProperty("AppId")
    private Integer appId;
    @ApiModelProperty("用户来源")
    private Integer userOrigin;
    @ApiModelProperty("代理Id")
    private Integer agentId;
    @ApiModelProperty("上级Id")
    private Long parentId;
    @ApiModelProperty("用户类型")
    private Integer userType;
    @ApiModelProperty("用户分级")
    private Integer userLevel;
    @ApiModelProperty("系统类型")
    private Integer equipmentType;
    @ApiModelProperty("设备类型")
    private String mobileType;


}
