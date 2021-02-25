package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_advertising_config")
public class LongVideoAdvertisingConfigEntity {

    private Integer id;
    private String adConfigKey;
    private String adConfigValue;
    private String adRemark;

}
