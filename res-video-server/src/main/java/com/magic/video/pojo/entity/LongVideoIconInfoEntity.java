package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_icon_info")
public class LongVideoIconInfoEntity {

    private Integer iconId;
    private Integer classifyId;
    private String iconPath;
    private String iconRealPath;

}
