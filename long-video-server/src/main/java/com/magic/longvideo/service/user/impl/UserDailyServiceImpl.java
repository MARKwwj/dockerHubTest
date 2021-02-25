package com.magic.longvideo.service.user.impl;

import cn.hutool.core.date.DateTime;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicDateUtil;
import com.magic.longvideo.service.user.UserDailyService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserDailyServiceImpl implements UserDailyService {

    private static final String USER_TODAY_DOWNLOAD_VIDEO_COUNT_PREFIX = "USER_TODAY_DOWNLOAD_VIDEO_COUNT_";

    @Override
    public Long getTodayDownloadCount(Long userId) {
        Long totalDownloadCount = JedisUtil.incr(USER_TODAY_DOWNLOAD_VIDEO_COUNT_PREFIX + userId);
        Date today = new Date();
        DateTime dateTimeOfEndDay = MagicDateUtil.endOfDay(today);
        int endOfDaySecond = (int) ((dateTimeOfEndDay.getTime() - today.getTime()) / 1000);
        JedisUtil.expireNotExist(USER_TODAY_DOWNLOAD_VIDEO_COUNT_PREFIX + userId, endOfDaySecond);
        return totalDownloadCount;
    }
}
