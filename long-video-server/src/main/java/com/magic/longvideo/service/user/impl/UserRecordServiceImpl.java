package com.magic.longvideo.service.user.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoPaymentOrderRecordMapper;
import com.magic.longvideo.mapper.LongVideoUserCoinDetailsMapper;
import com.magic.longvideo.pojo.dto.LongVideoPaymentOrderRecordDto;
import com.magic.longvideo.pojo.dto.RechargeRecordDto;
import com.magic.longvideo.pojo.entity.LongVideoPaymentOrderRecordEntity;
import com.magic.longvideo.pojo.entity.LongVideoUserCoinDetailsEntity;
import com.magic.longvideo.service.user.UserRecordService;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class UserRecordServiceImpl implements UserRecordService {

    private final LongVideoUserCoinDetailsMapper longVideoUserCoinDetailsMapper;
    private final LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper;

    public UserRecordServiceImpl(
            LongVideoUserCoinDetailsMapper longVideoUserCoinDetailsMapper,
            LongVideoPaymentOrderRecordMapper longVideoPaymentOrderRecordMapper) {
        this.longVideoUserCoinDetailsMapper = longVideoUserCoinDetailsMapper;
        this.longVideoPaymentOrderRecordMapper = longVideoPaymentOrderRecordMapper;
    }

    @Override
    public List<LongVideoPaymentOrderRecordDto> getRechargeRecord(Long userId) {
        QueryWrapper<LongVideoPaymentOrderRecordEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<LongVideoPaymentOrderRecordEntity> longVideoPaymentOrderRecordEntityList = longVideoPaymentOrderRecordMapper.selectList(queryWrapper);
        return MagicBeanUtil.copyListProperties(longVideoPaymentOrderRecordEntityList,LongVideoPaymentOrderRecordDto::new);
    }

    @Override
    public List<RechargeRecordDto> getCoinDetails(Long userId) {
        QueryWrapper<LongVideoUserCoinDetailsEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        LongVideoUserCoinDetailsEntity longVideoUserCoinDetailsEntity = longVideoUserCoinDetailsMapper.selectOne(queryWrapper);
        if (longVideoUserCoinDetailsEntity == null) {
            return null;
        }
        JSONArray jsonArray = JSONUtil.parseArray(longVideoUserCoinDetailsEntity.getCoinDetails());
        LinkedList<RechargeRecordDto> rechargeRecordDtoList = new LinkedList<>();
        Long maxDateTime = 0L;
        for (JSONObject jsonObject : jsonArray.jsonIter()) {
            Long curDateTime = jsonObject.getLong("purchaseDate");
            if (curDateTime > maxDateTime) {
                rechargeRecordDtoList.addFirst(jsonObject.toBean(RechargeRecordDto.class));
            } else {
                rechargeRecordDtoList.addLast(jsonObject.toBean(RechargeRecordDto.class));
            }
            maxDateTime = curDateTime > maxDateTime ? maxDateTime : curDateTime;
        }
        return rechargeRecordDtoList;
    }
}
