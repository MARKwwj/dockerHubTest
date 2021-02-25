package com.magic.payment.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_commodity_info")
public class LongVideoPaymentCommodityInfoEntity {

    @TableId
    private Integer commodityId;
    private String commodityName;
    private Integer commodityType;
    private Integer commodityValue;
    private Integer commodityGift;
    private Integer commodityInFactAmount;
    private Integer commodityBeforeDiscountAmount;
    private String commodityIntroduce;
    private String commodityPointCardIntroduce;
    private Integer commodityConsumePoint;
    private Integer isRecommend;
    private Integer sort;

}
