package com.magic.payment.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_payment_merchant_config")
public class LongVideoPaymentMerchantConfigEntity {

    @TableId
    private Integer merchantId;
    private String merchantCode;
    private String merchantName;
    private String merchantProductCode;
    private String merchantPrivateKey;
    private int status;
    private String paymentApiUrl;
    private String notifyUrl;
    private java.util.Date createTime;
    private String createBy;
    private String mchRemark;
    private int delFlag;

}
