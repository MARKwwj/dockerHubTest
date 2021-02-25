package com.magic.sms.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_sms_template")
public class LongVideoSmsTemplateEntity {

    @TableId
    private Integer templateId;
    private String channelTemplateNo;
    private String templateName;
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private String signName;
    private Integer status;
    private Integer merchantId;

}
