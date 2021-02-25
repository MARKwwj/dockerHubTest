package com.magic.payment.pojo.entity;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.beans.Transient;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_order_record")
public class LongVideoPaymentOrderRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long recordId;
    private Long userId;
    private String nickName;
    private String commodityName;
    private Integer commodityValue;
    private Integer commodityGift;
    private Integer commodityInFactAmount;
    private Integer commodityType;
    private Integer orderStatus;
    private Integer rechargeMethod;
    private String orderNumber;
    private String thirdPartyOrderNumber;
    private Integer delFlag;
    private java.util.Date createTime;
    private java.util.Date successTime;
    private Integer coinBalance;
    private java.util.Date vipEndTime;
    private Integer channelId;
    private Double rateAmount;
    private Double rechargeAmount;

    /**
     * 是否为官方用户充值，0否，1是
     */
    private Integer isOfficial;
}
