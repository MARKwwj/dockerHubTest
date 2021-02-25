package com.magic.sms.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_sms_config")
public class LongVideoSmsConfigEntity {

    @TableId
    private Integer appId;
    private Integer passageId;
    private Integer smsMerchantId;
    private Integer templateId;

}
