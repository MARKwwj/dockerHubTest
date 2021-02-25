package com.magic.sms.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_sms_passage")
public class LongVideoSmsPassageEntity {

    @TableId
    private Integer passageId;
    private String passageName;
    private Integer status;
    private java.util.Date createTime;
    private java.util.Date updateTime;
    private String passageRemark;

}
