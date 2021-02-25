package com.magic.longvideo.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_user_coin_details")
public class LongVideoUserCoinDetailsEntity {

    @TableId
    private Long userId;
    private String coinDetails;

}
