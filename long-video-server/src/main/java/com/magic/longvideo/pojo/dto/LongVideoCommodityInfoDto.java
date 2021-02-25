package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class LongVideoCommodityInfoDto {

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

}
