package com.magic.longvideo.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.magic.longvideo.pojo.entity.LongVideoPaymentCommodityChannelRelationEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface LongVideoPaymentCommodityChannelRelationMapper extends BaseMapper<LongVideoPaymentCommodityChannelRelationEntity> {

    @Select("select channel_id from long_video_payment_commodity_channel_relation ${ew.customSqlSegment}")
    List<Integer> selecChannelIdtListByCommodityId(@Param(Constants.WRAPPER) Wrapper wrapper);
}
