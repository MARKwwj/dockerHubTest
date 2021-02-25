package com.magic.longvideo.service.system.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.longvideo.mapper.LongVideoAppConfigMapper;
import com.magic.longvideo.pojo.entity.LongVideoAppConfigEntity;
import com.magic.longvideo.service.system.ConfigService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigServiceImpl implements ConfigService {

    private final LongVideoAppConfigMapper longVideoAppConfigMapper;

    public ConfigServiceImpl(LongVideoAppConfigMapper longVideoAppConfigMapper) {
        this.longVideoAppConfigMapper = longVideoAppConfigMapper;
    }

    @Override
    public JSONObject info(List<String> keys) {
        QueryWrapper<LongVideoAppConfigEntity> queryWrapper = new QueryWrapper();
        QueryWrapper<LongVideoAppConfigEntity> config_key = queryWrapper.in("config_key", keys);
        List<LongVideoAppConfigEntity> longVideoAppConfigEntityList = longVideoAppConfigMapper.selectList(config_key);
        Map<String, Object> configMap = new HashMap<>(longVideoAppConfigEntityList.size());
        for (LongVideoAppConfigEntity entity : longVideoAppConfigEntityList) {
            String configValue = entity.getConfigValue();
            if(JSONUtil.isJson(String.valueOf(configValue))){
                configMap.put(entity.getConfigKey(), JSONUtil.parseObj(configValue));
            }else{
                configMap.put(entity.getConfigKey(), configValue);
            }
        }
        return JSONUtil.parseObj(configMap);
    }
}
