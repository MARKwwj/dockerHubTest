package com.magic.payment.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_commodity_channel_relation")
public class LongVideoPaymentCommodityChannelRelationEntity {

    private Integer relationId;
    private Integer commodityId;
    private Integer channelId;

}
