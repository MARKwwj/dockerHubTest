package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_order_record")
public class LongVideoPaymentOrderRecordEntity {

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
    private Double rechargeAmount;
    private Integer channelId;
    private Double rateAmount;
    private Integer isOfficial;
    private Integer isOutGrade;
    private Integer isDeduct;

}
