package com.magic.longvideo.pojo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class LongVideoFeedbackRecordDto {
    private String feedbackContent;
    private String problemTagName;
    private int statusFlag;
    private String reply;
    private Date createTime;
    private Date replyTime;
}
