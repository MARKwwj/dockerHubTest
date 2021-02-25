package com.magic.sms.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;


@Getter
@Builder
@ToString
@Accessors(chain = true)
@TableName("long_video_sms_record")
public class LongVideoSmsRecordEntity {

    @TableId(type = IdType.AUTO)
    private Integer id;
    private String phoneNumber;
    private String templateParam;
    private Integer templateId;
    private String sendCode;
    private String sendResult;
    private Long userId;
    private java.util.Date createTime;

}
