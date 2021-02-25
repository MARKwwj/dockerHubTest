package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_user")
public class LongVideoUserEntity {
    @TableId(type= IdType.AUTO)
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
