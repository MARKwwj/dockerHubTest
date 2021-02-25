package com.magic.longvideo.service.payment.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoPaymentChannelMapper;
import com.magic.longvideo.mapper.LongVideoPaymentCommodityChannelRelationMapper;
import com.magic.longvideo.pojo.dto.LongVideoPaymentChannelDto;
import com.magic.longvideo.pojo.entity.LongVideoPaymentChannelEntity;
import com.magic.longvideo.pojo.entity.LongVideoPaymentCommodityChannelRelationEntity;
import com.magic.longvideo.service.payment.PaymentService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final LongVideoPaymentCommodityChannelRelationMapper longVideoPaymentCommodityChannelRelationMapper;


    private final LongVideoPaymentChannelMapper longVideoPaymentChannelMapper;

    public PaymentServiceImpl(LongVideoPaymentCommodityChannelRelationMapper longVideoPaymentCommodityChannelRelationMapper, LongVideoPaymentChannelMapper longVideoPaymentChannelMapper) {
        this.longVideoPaymentCommodityChannelRelationMapper = longVideoPaymentCommodityChannelRelationMapper;
        this.longVideoPaymentChannelMapper = longVideoPaymentChannelMapper;
    }

    @Override
    public List<LongVideoPaymentChannelDto> payMethod(int commodityId) {
        Map<Integer, String> channelIcon = new HashMap<>();
        channelIcon.put(1,"1");
        channelIcon.put(2,"1");
        channelIcon.put(3,"1");
        channelIcon.put(4,"1");
        channelIcon.put(5,"1");
        List<Integer> channelIdList = longVideoPaymentCommodityChannelRelationMapper.selecChannelIdtListByCommodityId(new QueryWrapper<LongVideoPaymentCommodityChannelRelationEntity>().eq("commodity_id", commodityId));
        if (channelIdList.size() == 0) {
            return Collections.emptyList();
        }
        List<LongVideoPaymentChannelEntity> longVideoPaymentChannelEntityList = longVideoPaymentChannelMapper.selectList(new QueryWrapper<LongVideoPaymentChannelEntity>()
                .in("channel_id", channelIdList)
                .orderByAsc("sort")
                .eq("del_flag", 2)
        );
        List<LongVideoPaymentChannelDto> longVideoPaymentChannelDtoList = MagicBeanUtil.copyListProperties(longVideoPaymentChannelEntityList, LongVideoPaymentChannelDto::new, (source, target) -> {
            target.setChannelIcon(channelIcon.get(source.getChannelType()));
        });
        return longVideoPaymentChannelDtoList;
    }
}
