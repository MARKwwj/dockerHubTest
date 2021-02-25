package com.magic.longvideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserHistoryEntity;

public interface LongVideoUserHistoryMapper extends BaseMapper<LongVideoUserHistoryEntity> {

    int insertOnDuplicateKeyUpdate(LongVideoUserHistoryEntity entity);
}
