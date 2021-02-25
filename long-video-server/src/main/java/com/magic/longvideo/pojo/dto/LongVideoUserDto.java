package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class LongVideoUserDto {
    private Long userId;
    private String nickName;
    private String machineCode;
    private Long parentId;
    private Integer agentId;
    private Integer userLevel;
    private String avatar;
    private Integer userType;
    private Integer goldCoins;
    private String phoneNumber;
    private java.util.Date bindTime;
    private Integer userOrigin;
    private Integer status;
    private String mobileType;
    private Integer equipmentType;
    private Integer deduction;
    private Integer totalRecharge;
    private java.util.Date vipEndTime;
    private String loginIp;
    private java.util.Date createTime;
    private java.util.Date lastLoginTime;
}
