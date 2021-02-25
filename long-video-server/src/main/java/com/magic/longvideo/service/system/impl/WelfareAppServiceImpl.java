package com.magic.longvideo.service.system.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoWelfareConfigMapper;
import com.magic.longvideo.pojo.dto.LongVideoWelfareConfigDto;
import com.magic.longvideo.pojo.entity.LongVideoWelfareConfigEntity;
import com.magic.longvideo.service.WelfareAppService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WelfareAppServiceImpl implements WelfareAppService {

    private final LongVideoWelfareConfigMapper longVideoWelfareConfigMapper;

    public WelfareAppServiceImpl(LongVideoWelfareConfigMapper longVideoWelfareConfigMapper) {
        this.longVideoWelfareConfigMapper = longVideoWelfareConfigMapper;
    }

    @Override
    public List<JSONObject> welfareAppList() {
        QueryWrapper<LongVideoWelfareConfigEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("del_flag", 2);
        queryWrapper.orderByAsc("sort");
        List<LongVideoWelfareConfigEntity> longVideoWelfareConfigEntityList = longVideoWelfareConfigMapper.selectList(queryWrapper);
        List<LongVideoWelfareConfigDto> longVideoWelfareConfigDtoList = MagicBeanUtil.copyListProperties(longVideoWelfareConfigEntityList, LongVideoWelfareConfigDto::new);
        List<JSONObject> result = new ArrayList<>();
        JSONObject featuredApp = new JSONObject().putOnce("name","精品应用");
        JSONObject hotApp = new JSONObject().putOnce("name","热门应用");;
        result.add(featuredApp);
        result.add(hotApp);
        for (LongVideoWelfareConfigDto longVideoWelfareConfigDto : longVideoWelfareConfigDtoList) {
            if (longVideoWelfareConfigDto.getType().equals(1)) {
                featuredApp.append("apps", longVideoWelfareConfigDto);
            } else {
                hotApp.append("apps", longVideoWelfareConfigDto);
            }
        }
        return result;
    }

}
