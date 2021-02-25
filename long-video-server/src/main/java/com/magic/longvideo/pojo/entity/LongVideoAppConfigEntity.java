package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("long_video_app_config")
public class LongVideoAppConfigEntity {
    @TableId
    private Integer configId;
    private String configKey;
    private String configValue;
}
