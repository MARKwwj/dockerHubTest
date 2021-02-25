package com.magic.longvideo.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.longvideo.mapper.LongVideoNoticeInfoMapper;
import com.magic.longvideo.pojo.dto.LoopNoticeDto;
import com.magic.longvideo.pojo.dto.PopNoticeDto;
import com.magic.longvideo.pojo.dto.SystemNoticeDto;
import com.magic.longvideo.pojo.entity.LongVideoNoticeInfoEntity;
import com.magic.longvideo.service.system.NoticeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NoticeServiceImpl implements NoticeService {


    private static final int HOME_PAGE_LOOP_NOTICE = 1;

    private static final int WELFARE_LOOP_NOTICE = 2;

    private static final int HOME_PAGE_POP_NOTICE = 3;

    private static final int NOTICE_LIST = 4;

    private final LongVideoNoticeInfoMapper longVideoNoticeInfoMapper;

    public NoticeServiceImpl(LongVideoNoticeInfoMapper longVideoNoticeInfoMapper) {
        this.longVideoNoticeInfoMapper = longVideoNoticeInfoMapper;
    }

    private static final String SYSTEM_NOTICE_IS_READ_PREFIX = "SYSTEM_NOTICE_IS_READ_";

    @Override
    public List<SystemNoticeDto> systemNotice(Long userId) {
        QueryWrapper<LongVideoNoticeInfoEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", NOTICE_LIST);
        queryWrapper.eq("state", 1);
        List<LongVideoNoticeInfoEntity> longVideoNoticeInfoEntityList = longVideoNoticeInfoMapper.selectList(queryWrapper);
        List<SystemNoticeDto> systemNoticeDtoList = MagicBeanUtil.copyListProperties(longVideoNoticeInfoEntityList, SystemNoticeDto::new);
        //TODO 判断用户是否已读
        Map<String, String> noticeIsReadMap = JedisUtil.hGet(SYSTEM_NOTICE_IS_READ_PREFIX + userId);
        for (SystemNoticeDto systemNoticeDto : systemNoticeDtoList) {
            if (noticeIsReadMap.get(String.valueOf(systemNoticeDto.getNoticeId())) != null) {
                systemNoticeDto.setRead(true);
            }
        }
        return systemNoticeDtoList;
    }

    @Override
    public boolean readSystemNotice(Long userId, Integer noticeId) {
        return JedisUtil.hSet(SYSTEM_NOTICE_IS_READ_PREFIX + userId, String.valueOf(noticeId), String.valueOf(noticeId));
    }


    @Override
    public PopNoticeDto popNotice() {
        QueryWrapper<LongVideoNoticeInfoEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", HOME_PAGE_POP_NOTICE);
        queryWrapper.eq("state", 1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        LongVideoNoticeInfoEntity longVideoNoticeInfoEntity = longVideoNoticeInfoMapper.selectOne(queryWrapper);
        PopNoticeDto popNoticeDto = new PopNoticeDto();
        MagicBeanUtil.copyProperties(longVideoNoticeInfoEntity, popNoticeDto);
        return popNoticeDto;
    }

    /**
     * 后台赠送金币或VIP给用户后会读取存在Redis中的 GIFT_NOTICE_PREFIX + userId
     */
    private static final String GIFT_NOTICE_PREFIX = "GIFT_NOTICE_";

    @Override
    public List<String> giftNotice(Long userId) {
        List<String> noticeList = JedisUtil.lRange(GIFT_NOTICE_PREFIX + userId);
        JedisUtil.del(GIFT_NOTICE_PREFIX + userId);
        return noticeList;
    }


    @Override
    public LoopNoticeDto loopNotice(Integer type) {
        QueryWrapper<LongVideoNoticeInfoEntity> queryWrapper = new QueryWrapper();
        queryWrapper.eq("type", type);
        queryWrapper.eq("state", 1);
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("limit 1");
        LongVideoNoticeInfoEntity longVideoNoticeInfoEntity = longVideoNoticeInfoMapper.selectOne(queryWrapper);
        LoopNoticeDto loopNoticeDto = new LoopNoticeDto();
        MagicBeanUtil.copyProperties(longVideoNoticeInfoEntity, loopNoticeDto);
        return loopNoticeDto;
    }


}
