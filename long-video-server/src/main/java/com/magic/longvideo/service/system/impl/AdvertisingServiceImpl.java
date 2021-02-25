package com.magic.longvideo.service.system.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoAdvertisingConfigMapper;
import com.magic.longvideo.mapper.LongVideoAdvertisingInfoMapper;
import com.magic.longvideo.pojo.dto.LongVideoAdvertisingInfoDto;
import com.magic.longvideo.pojo.entity.LongVideoAdvertisingConfigEntity;
import com.magic.longvideo.pojo.entity.LongVideoAdvertisingInfoEntity;
import com.magic.longvideo.service.system.AdvertisingService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdvertisingServiceImpl implements AdvertisingService {

    private final LongVideoAdvertisingConfigMapper longVideoAdvertisingConfigMapper;
    private final LongVideoAdvertisingInfoMapper longVideoAdvertisingInfoMapper;

    public AdvertisingServiceImpl(LongVideoAdvertisingConfigMapper longVideoAdvertisingConfigMapper, LongVideoAdvertisingInfoMapper longVideoAdvertisingInfoMapper) {
        this.longVideoAdvertisingConfigMapper = longVideoAdvertisingConfigMapper;
        this.longVideoAdvertisingInfoMapper = longVideoAdvertisingInfoMapper;
    }

    @Override
    public Map<String, List<LongVideoAdvertisingInfoDto>> advertisingList() {
        List<LongVideoAdvertisingInfoEntity> longVideoAdvertisingInfoEntityList = longVideoAdvertisingInfoMapper.selectList(null);
        List<LongVideoAdvertisingInfoDto> longVideoAdvertisingInfoDtoList = MagicBeanUtil.copyListProperties(longVideoAdvertisingInfoEntityList, LongVideoAdvertisingInfoDto::new);
        Map<Integer, LongVideoAdvertisingInfoDto> longVideoAdvertisingInfoEntityMap = new HashMap<>(longVideoAdvertisingInfoEntityList.size());
        for (LongVideoAdvertisingInfoDto dto : longVideoAdvertisingInfoDtoList) {
            longVideoAdvertisingInfoEntityMap.put(dto.getAdId(), dto);
        }
        List<LongVideoAdvertisingConfigEntity> longVideoAdvertisingConfigEntityList = longVideoAdvertisingConfigMapper.selectList(null);
        Map<String, List<LongVideoAdvertisingInfoDto>> result = new HashMap<>(longVideoAdvertisingConfigEntityList.size());
        for (LongVideoAdvertisingConfigEntity entity : longVideoAdvertisingConfigEntityList) {
            JSONArray adConfigValue = JSONUtil.parseArray(entity.getAdConfigValue());
            List<LongVideoAdvertisingInfoDto> dtoList = new ArrayList<>();
            for (Object adId : adConfigValue) {
                dtoList.add(longVideoAdvertisingInfoEntityMap.get(adId));
            }
            result.put(entity.getAdConfigKey(), dtoList);
        }
        return result;
    }

    @Override
    public List<LongVideoAdvertisingInfoDto> specify(String adConfigKey) {
        List<LongVideoAdvertisingInfoEntity> longVideoAdvertisingInfoEntityList = longVideoAdvertisingInfoMapper.selectList(null);
        List<LongVideoAdvertisingInfoDto> longVideoAdvertisingInfoDtoList = MagicBeanUtil.copyListProperties(longVideoAdvertisingInfoEntityList, LongVideoAdvertisingInfoDto::new);
        Map<Integer, LongVideoAdvertisingInfoDto> longVideoAdvertisingInfoEntityMap = new HashMap<>(longVideoAdvertisingInfoEntityList.size());
        for (LongVideoAdvertisingInfoDto dto : longVideoAdvertisingInfoDtoList) {
            longVideoAdvertisingInfoEntityMap.put(dto.getAdId(), dto);
        }
        QueryWrapper<LongVideoAdvertisingConfigEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("ad_config_key", adConfigKey);
        LongVideoAdvertisingConfigEntity longVideoAdvertisingConfigEntity = longVideoAdvertisingConfigMapper.selectOne(queryWrapper);
        JSONArray adConfigValue = JSONUtil.parseArray(longVideoAdvertisingConfigEntity.getAdConfigValue());
        List<LongVideoAdvertisingInfoDto> dtoList = new ArrayList<>();
        for (Object adId : adConfigValue) {
            dtoList.add(longVideoAdvertisingInfoEntityMap.get(adId));
        }
        return dtoList;
    }
}
