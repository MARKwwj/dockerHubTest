package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_video_info")
public class LongVideoVideoInfoEntity {
    @TableId(type= IdType.AUTO)
    private Integer videoId;
    private String videoTitle;
    private String videoIntro;
    private String videoCover;
    private Integer videoDuration;
    private String videoSerialNum;
    private String videoTags;
    private Integer videoMachineId;
    private String videoMachineName;
    private String videoUrl;
    private Integer videoPayType;
    private int videoPayCoin;
    private Double videoScore;
    private Long videoPlayCount;
    private Long videoPraiseCount;
    private Long videoFavoriteCount;
    private Integer videoStatus;
    private String videoCreator;
    private java.util.Date videoCreateTime;
    private String videoUpdater;
    private java.util.Date videoUpdateTime;
    private Long videoByteSize;
    private java.util.Date videoFreeExpires;

}
