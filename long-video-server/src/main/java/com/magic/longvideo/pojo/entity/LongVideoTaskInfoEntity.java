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
@TableName("long_video_task_info")
public class LongVideoTaskInfoEntity {

    @TableId(type = IdType.AUTO)
    private Integer taskId;
    private String taskContent;
    private String taskAwardContent;
    private int taskType;
    private String taskLogo;
    private int awardType;
    private Integer award;
    private Integer target;
    private Integer sort;
    private int taskMode;
    private String taskReceiveNotice;

}
