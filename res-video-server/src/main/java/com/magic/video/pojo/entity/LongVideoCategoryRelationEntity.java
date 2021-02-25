package com.magic.video.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_category_relation")
public class LongVideoCategoryRelationEntity {

    private Integer relationId;
    private Integer categoryId;
    private Integer videoId;

}
