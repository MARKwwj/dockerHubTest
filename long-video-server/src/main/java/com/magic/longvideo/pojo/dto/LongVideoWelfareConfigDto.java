package com.magic.longvideo.pojo.dto;

import lombok.Data;

@Data
public class LongVideoWelfareConfigDto {
    private Integer id;
    private String appName;
    private Integer appId;
    private String picUrl;
    private Integer type;
    private Integer downloadNum;
    private Integer status;
    private Double size;
    private String introduce;
    private String linkUrl;
}
