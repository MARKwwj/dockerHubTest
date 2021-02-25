package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_model_info")
public class LongVideoModelInfoEntity {

    private Integer modelId;
    private String modelName;
    private String modelUid;
    private Integer modelIndex;
    private Integer categoryId;
    private String modelTemplateName;

}
