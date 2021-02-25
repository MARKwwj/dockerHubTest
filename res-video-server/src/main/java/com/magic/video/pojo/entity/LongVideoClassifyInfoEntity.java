package com.magic.video.pojo.entity;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableName;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_classify_info")
public class LongVideoClassifyInfoEntity {

    private Integer classifyId;
    private Integer categoryId;
    private String classifyName;
    private Integer classifySort;

}
