package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_user_praise")
public class LongVideoUserPraiseEntity {

    private Integer userId;
    private String videoPraiseInfos;

}
