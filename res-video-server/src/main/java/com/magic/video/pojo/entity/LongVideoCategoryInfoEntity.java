package com.magic.video.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_category_info")
public class LongVideoCategoryInfoEntity {

    private Integer categoryId;
    private String categoryName;
    private Integer categoryOrder;
    private String categoryRemark;
    private String templateName;
}
