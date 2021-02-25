package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_tag_info")
public class LongVideoTagInfoEntity {

    private Integer tagId;
    private String tagName;
    private Integer tagOrder;
    private Integer parentId;

}
