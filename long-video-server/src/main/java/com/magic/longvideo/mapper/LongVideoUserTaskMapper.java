package com.magic.longvideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserTaskEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface LongVideoUserTaskMapper extends BaseMapper<LongVideoUserTaskEntity> {
    @Insert("INSERT INTO long_video_user_task (user_id,${longVideoUserTaskTableFieldName}) values(#{userId},#{userTaskMapJson}) ON DUPLICATE KEY UPDATE ${longVideoUserTaskTableFieldName}=#{userTaskMapJson}")
    void insertOnDuplicateKeyUpdate(@Param("userId") Long userId,@Param("userTaskMapJson") String userTaskMapJson,@Param("longVideoUserTaskTableFieldName") String longVideoUserTaskTableFieldName);
}
