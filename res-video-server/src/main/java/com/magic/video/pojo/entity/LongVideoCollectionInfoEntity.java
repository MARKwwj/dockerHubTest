package com.magic.video.pojo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;


@Data
@ToString
@Accessors(chain = true)
@TableName("long_video_collection_info")
public class LongVideoCollectionInfoEntity {

    private Integer collectionId;
    private Integer categoryId;
    private String collectionName;
    private Integer collectionSort;
    private Integer collectionIconId;
    private String showType;
    private String action;
    private String moreShowType;
    private String showTypeBottomItemBuilder;
    private String showTypeTopItemBuilder;
    private String moreShowTypeBottomItemBuilder;
    private String moreShowTypeTopItemBuilder;
    private String apiUrl;
    private String moreTemplateName;

}
