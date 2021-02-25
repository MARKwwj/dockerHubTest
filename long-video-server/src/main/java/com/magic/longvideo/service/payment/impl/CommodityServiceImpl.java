package com.magic.longvideo.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoPaymentCommodityInfoMapper;
import com.magic.longvideo.pojo.dto.LongVideoCommodityInfoDto;
import com.magic.longvideo.pojo.entity.LongVideoPaymentCommodityInfoEntity;
import com.magic.longvideo.service.payment.CommodityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommodityServiceImpl implements CommodityService {

    private final LongVideoPaymentCommodityInfoMapper longVideoPaymentCommodityInfoMapper;

    public CommodityServiceImpl(LongVideoPaymentCommodityInfoMapper longVideoPaymentCommodityInfoMapper) {
        this.longVideoPaymentCommodityInfoMapper = longVideoPaymentCommodityInfoMapper;
    }

    @Override
    public List<LongVideoCommodityInfoDto> info(int commodityType) {
        QueryWrapper<LongVideoPaymentCommodityInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("commodity_type", commodityType);
        List<LongVideoPaymentCommodityInfoEntity> longVideoCommodityInfoEntityList = longVideoPaymentCommodityInfoMapper.selectList(queryWrapper);
        List<LongVideoCommodityInfoDto> longVideoCommodityInfoDtoList = MagicBeanUtil.copyListProperties(longVideoCommodityInfoEntityList, LongVideoCommodityInfoDto::new);
        return longVideoCommodityInfoDtoList;
    }

}
