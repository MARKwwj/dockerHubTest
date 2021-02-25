package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_feedback_problem_tag")
public class LongVideoFeedbackProblemTagEntity {

    private Long problemTagId;
    private String problemTagName;
    private Integer sort;

}
