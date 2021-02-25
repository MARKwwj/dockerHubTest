package com.magic.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.video.pojo.entity.LongVideoClassifyRelationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LongVideoClassifyRelationMapper extends BaseMapper<LongVideoClassifyRelationEntity> {
    @Select("select video_id from long_video_classify_relation where classify_id=#{classifyId}")
    Integer[] selectVideoIdsByClassifyId(Integer classifyId);
}
