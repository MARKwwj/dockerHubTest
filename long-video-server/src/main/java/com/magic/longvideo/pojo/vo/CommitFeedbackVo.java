package com.magic.longvideo.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class CommitFeedbackVo {

    private String nickName;
    private String email;
    private String feedbackContent;
    private String problemTagName;
    private List<String> urls;
}
