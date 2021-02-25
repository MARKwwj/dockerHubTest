package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_app_version")
public class LongVideoAppVersionEntity {

    @TableId
    private Integer versionId;
    private Integer client;
    private Integer type;
    private String packageAddr;
    private String versionNum;
    private String updContent;

}
