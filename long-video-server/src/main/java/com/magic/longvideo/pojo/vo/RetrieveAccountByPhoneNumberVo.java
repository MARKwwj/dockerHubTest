package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RetrieveAccountByPhoneNumberVo {

    @ApiModelProperty("手机号")
    private String phoneNumber;
    @ApiModelProperty("验证码")
    private String verificationCode;
    @ApiModelProperty("机器码")
    private String machineCode;

}
