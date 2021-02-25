package com.magic.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.video.pojo.entity.LongVideoCategoryRelationEntity;
import org.apache.ibatis.annotations.Select;

public interface LongVideoCategoryRelationMapper extends BaseMapper<LongVideoCategoryRelationEntity> {

    @Select("select video_id from long_video_category_relation where category_id=#{categoryId}")
    Integer[] selectVideoIdsByCategoryId(Integer categoryId);
}
