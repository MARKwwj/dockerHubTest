package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_feedback_record")
public class LongVideoFeedbackRecordEntity {

    @TableId(type = IdType.AUTO)
    private Long feedbackId;
    private String feedbackContent;
    private String problemTagName;
    private String email;
    private String urls;
    private Long userId;
    private String nickName;
    private Integer delFlag;
    private Integer statusFlag;
    private String reply;
    private java.util.Date createTime;
    private java.util.Date replyTime;

}
