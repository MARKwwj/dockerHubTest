package com.magic.longvideo.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_app_realm")
public class LongVideoAppRealmEntity {

    private Integer id;
    private Integer appId;
    private String realmUrl;
    private Integer status;
    private java.util.Date createTime;
    private Integer delFlag;

}
