package com.magic.sms.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_sms_merchant_info")
public class LongVideoSmsMerchantInfoEntity {

    @TableId
    private Integer smsMerchantId;
    private String smsMerchantName;
    private String privateKey;
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private Integer status;
    private String privateKeyPassword;
    private Integer passageId;

}
