package com.magic.longvideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserFavoriteEntity;

public interface LongVideoUserFavoriteMapper extends BaseMapper<LongVideoUserFavoriteEntity> {


    int insertOnDuplicateKeyUpdate(LongVideoUserFavoriteEntity entity);
}
