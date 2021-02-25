package com.magic.payment.pojo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("分成扣量参数Model")
public class ProfitParamDto {

    @ApiModelProperty("app用户id")
    private Long userId;
    @ApiModelProperty("代理id")
    private Integer agentId;
    @ApiModelProperty("appId")
    private Integer appId;
    // 用户类型 0直充1点卡
    @ApiModelProperty("用户类型")
    private Integer userType;
    @ApiModelProperty("订单id")
    private String payOrderId;
    @ApiModelProperty("原订单id")
    private String mchOrderId;
    @ApiModelProperty("订单金额")
    private String orderAmount;
    @ApiModelProperty("时间")
    private java.util.Date createTime;
    @ApiModelProperty("设备型号")
    private Integer equipmentType;
}
