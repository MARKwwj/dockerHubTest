package com.magic.longvideo.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_common_problem")
public class LongVideoCommonProblemEntity {

    private Long problemId;
    private String problem;
    private String answer;
    private Integer sort;
    private int delFlag;

}
