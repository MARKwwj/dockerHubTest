package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_notice_info")
public class LongVideoNoticeInfoEntity {

    private Integer noticeId;
    private Integer type;
    private String title;
    private String content;
    private Integer state;
    private java.util.Date createTime;
    private Integer count;

}
