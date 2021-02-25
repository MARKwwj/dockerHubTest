package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class SystemNoticeDto {
    private Integer noticeId;
    private String title;
    private String content;
    private java.util.Date createTime;
    private boolean isRead;
}
