package com.magic.video.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.magic.video.pojo.entity.LongVideoTagRelationEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LongVideoTagRelationMapper extends BaseMapper<LongVideoTagRelationEntity> {

    @Select("select tag_name from long_video_tag_relation t1 " +
            "left join long_video_tag_info t2 on t1.tag_id=t2.tag_id " +
            "${ew.customSqlSegment}")
    List<String> selectTagIdByVideoId(@Param(Constants.WRAPPER) Wrapper wrapper);
}
