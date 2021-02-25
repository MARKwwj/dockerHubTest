package com.magic.longvideo.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class RetrieveAccountByQrCodeVo {
    @ApiModelProperty("旧机器码")
    private String oldMachineCode;
    @ApiModelProperty("新机器码")
    private String newMachineCode;
    @ApiModelProperty("旧token")
    private String oldToken;
}
