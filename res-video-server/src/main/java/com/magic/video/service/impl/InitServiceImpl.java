package com.magic.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.video.mapper.LongVideoHostInfoMapper;
import com.magic.video.pojo.entity.LongVideoHostInfoEntity;
import com.magic.video.service.InitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InitServiceImpl implements InitService {

    @Autowired
    private LongVideoHostInfoMapper longVideoHostInfoMapper;

    @Override
    public Map<String, String> machineDomain() {
        QueryWrapper<LongVideoHostInfoEntity> queryWrapper = new QueryWrapper();
        queryWrapper.select("machine_id", "host_name");
        List<LongVideoHostInfoEntity> longVideoHostInfoEntityList = longVideoHostInfoMapper.selectList(queryWrapper);
        Map<String, String> result = new HashMap<>(longVideoHostInfoEntityList.size());
        for (LongVideoHostInfoEntity entity : longVideoHostInfoEntityList) {
            result.put(String.valueOf(entity.getMachineId()), entity.getHostName());
        }
        return result;


    }
}
