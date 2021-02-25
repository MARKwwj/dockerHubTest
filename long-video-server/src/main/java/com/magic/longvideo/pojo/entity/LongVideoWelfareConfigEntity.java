package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_welfare_config")
public class LongVideoWelfareConfigEntity {

    private Integer id;
    private String appName;
    private Integer appId;
    private String picUrl;
    private Integer type;
    private Integer downloadNum;
    private Double size;
    private String introduce;
    private Integer status;
    private String linkUrl;
    private Integer sort;
    private Integer delFlag;
    private java.util.Date createTime;
    private java.util.Date updateTime;

}
