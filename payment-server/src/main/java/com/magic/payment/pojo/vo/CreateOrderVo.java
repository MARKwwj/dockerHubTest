package com.magic.payment.pojo.vo;

import lombok.Data;

@Data
public class CreateOrderVo {

    private Integer channelId;
    private Integer commodityId;
    private Integer appType;
}
