package com.magic.longvideo.service.live.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONUtil;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicDateUtil;
import com.magic.longvideo.pojo.dto.LiveListDto;
import com.magic.longvideo.pojo.vo.LiveListVo;
import com.magic.longvideo.service.live.LiveService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LiveServiceImpl implements LiveService {

    private static final String LIVE_LIST_KEY_PREFIX = "LIVE_SEE_";
    private static final String SWITCH_KEY = "LIVE_SWITCH";

    @Override
    public Map<String, Object> liveList(LiveListVo liveListVo) {
        String liveSwitch = JedisUtil.hGet(SWITCH_KEY, liveListVo.getAppId());
        Long liveListLength = JedisUtil.lLen(LIVE_LIST_KEY_PREFIX + liveSwitch);
        PageUtil.setFirstPageNo(1);
        int start = PageUtil.getStart(liveListVo.getPageNum(), liveListVo.getPageSize());
        int stop = PageUtil.getEnd(liveListVo.getPageNum(), liveListVo.getPageSize());
        List<String> liveJsonStrList = JedisUtil.lRange(LIVE_LIST_KEY_PREFIX + liveSwitch, start, stop - 1);
        List<LiveListDto> liveList = new ArrayList<>();
        for (String jsonStr : liveJsonStrList) {
            liveList.add(JSONUtil.toBean(jsonStr, LiveListDto.class));
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("totalPage", PageUtil.totalPage(liveListLength.intValue(), liveListVo.getPageSize()));
        result.put("liveList", liveList);
        return result;
    }

    private static final String USER_WATCH_LIVE_TIME_PREFIX = "USER_WATCHED_LIVE_TIME_";

    @Override
    public Long watched(Integer time, Long userId) {
        Long totalWatchTime = JedisUtil.incrBy(USER_WATCH_LIVE_TIME_PREFIX + userId, time);
        Date today = new Date();
        DateTime dateTimeOfEndDay = MagicDateUtil.endOfDay(today);
        int endOfDaySecond = (int) ((dateTimeOfEndDay.getTime() - today.getTime()) / 1000);
        JedisUtil.expireNotExist(USER_WATCH_LIVE_TIME_PREFIX + userId, endOfDaySecond);
        return totalWatchTime;
    }

}
