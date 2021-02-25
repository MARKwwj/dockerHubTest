package com.magic.payment.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

/**
 * <p>
 * 长视频用户表
 * </p>
 *
 * @author pengfei.yang
 * @since 2020-12-11
 */
@Data
@TableName("long_video_user")
public class LongVideoUserEntity{

    /**
     * 用户id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 机器码
     */
    private String machineCode;

    /**
     * 上级id
     */
    private Long parentId;

    /**
     * 代理id
     */
    private Integer agentId;

    /**
     * 用户裂变层级
     */
    private Integer userLevel;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 用户类型（1,直充、2,点卡）
     */
    private Integer userType;

    /**
     * 用户金币余额
     */
    private Integer goldCoins;

    /**
     * 手机号
     */
    private String phoneNumber;

    /**
     * 绑定时间
     */
    private Date bindTime;

    /**
     * 用户来源	落地页： 0	渠道包：渠道包编号
     */
    private Integer userOrigin;

    /**
     * 用户状态（0正常 1停用）
     */
    private Integer status;

    /**
     * 设备型号
     */
    private String mobileType;

    /**
     * 设备类型(1 安卓, 2 ios ,3 其他 )
     */
    private Integer equipmentType;

    /**
     * 扣量（0未扣量,1已扣量）
     */
    private Integer deduction;

    /**
     * 总充值金额
     */
    private Integer totalRecharge;

    /**
     * vip到期时间
     */
    private Date vipEndTime;

    /**
     * 最后登录ip
     */
    private String loginIp;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户收藏的视频
     */
    private String videoFavoriteInfos;

    /**
     * 用户观看历史
     */
    private String videoHistoryInfos;

    /**
     * 用户点赞的视频
     */
    private String videoPraiseInfos;

    /**
     * 用户购买的视频
     */
    private String videoPurchasedInfos;


}
