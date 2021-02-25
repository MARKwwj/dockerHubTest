package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class LongVideoPaymentOrderRecordDto {

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
}
