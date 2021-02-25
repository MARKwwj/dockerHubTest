package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private Long userId;
    private String nickName;
    private String machineCode;
    private Integer agentId;
    private String avatar;
    private Integer goldCoins;
    private String phoneNumber;
    private String vipEndTime;
    private String bindCode;
    private Integer userType;
    private int rechargeStatus;
}
