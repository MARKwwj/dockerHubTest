package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_advertising_info")
public class LongVideoAdvertisingInfoEntity {

    private Integer adId;
    private String adTitle;
    private String adPic;
    private String adJumpParam;
    private Integer adValue;
    private Integer adType;
}
