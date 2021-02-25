package com.magic.payment.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_channel")
public class LongVideoPaymentChannelEntity {

    @TableId
    private Integer channelId;
    private Integer merchantId;
    private String channelName;
    private String channelCode;
    private int channelType;
    private int status;
    private Integer sort;
    private Integer pecuniaryUnit;
    private Double rate;
    private String limitAmount;

}
