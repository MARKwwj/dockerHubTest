package com.magic.payment.pojo.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderPrePayResultVO {

    private String orderNumber;
    private String html;
}
