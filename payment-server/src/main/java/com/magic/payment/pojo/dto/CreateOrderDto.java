package com.magic.payment.pojo.dto;

import lombok.Data;

@Data
public class CreateOrderDto {
    private Integer merchantId;
    private String merchantCode;
    private String merchantName;
    private String merchantProductCode;
    private String merchantPrivateKey;
    private String paymentApiUrl;
    private String notifyUrl;
    private String mchRemark;
    private Integer commodityId;
    private String commodityName;
    private Integer commodityType;
    private Integer commodityValue;
    private Integer commodityGift;
    private String commodityInFactAmount;
    private Integer commodityBeforeDiscountAmount;
    private Integer isRecommend;
    private String commodityIntroduce;
    private String pointCardName;
    private Integer buyPoint;
    private Integer sort;
    private Integer channelId;
    private String channelName;
    private String channelCode;
    private int channelType;
    private Integer pecuniaryUnit;
    private Double rate;
    private String limitAmount;
    private String orderNumber;
}
