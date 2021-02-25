package com.magic.longvideo.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoAppVersionMapper;
import com.magic.longvideo.pojo.dto.LongVideoAppVersionDto;
import com.magic.longvideo.pojo.entity.LongVideoAppVersionEntity;
import com.magic.longvideo.service.system.AppVersionService;
import org.springframework.stereotype.Service;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private final LongVideoAppVersionMapper longVideoAppVersionMapper;

    public AppVersionServiceImpl(LongVideoAppVersionMapper longVideoAppVersionMapper) {
        this.longVideoAppVersionMapper = longVideoAppVersionMapper;
    }

    @Override
    public LongVideoAppVersionDto version(Integer equipmentType) {
        QueryWrapper<LongVideoAppVersionEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("client", equipmentType);
        LongVideoAppVersionEntity longVideoAppVersionEntity = longVideoAppVersionMapper.selectOne(queryWrapper);
        LongVideoAppVersionDto longVideoAppVersionDto = MagicBeanUtil.copyProperties(longVideoAppVersionEntity, LongVideoAppVersionDto.class);
        return longVideoAppVersionDto;
    }
}
