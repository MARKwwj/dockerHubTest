package com.magic.search.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_category_relation")
public class LongVideoCategoryRelationEntity {

    private Integer relationId;
    private Integer categoryId;
    private Integer videoId;

}
