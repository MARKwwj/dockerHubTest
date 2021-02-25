package com.magic.longvideo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.magic.longvideo.pojo.entity.LongVideoUserPurchasedEntity;

public interface LongVideoUserPurchasedMapper extends BaseMapper<LongVideoUserPurchasedEntity> {
    int insertOnDuplicateKeyUpdate(LongVideoUserPurchasedEntity entity);
}
