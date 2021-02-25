package com.magic.longvideo.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.utils.MagicDateUtil;
import com.magic.longvideo.common.consts.UserVideoMapFieldName;
import com.magic.longvideo.common.consts.VideoRankType;
import com.magic.longvideo.mapper.*;
import com.magic.longvideo.pojo.dto.VideoInfoDto;
import com.magic.longvideo.pojo.dto.VideoPraiseDto;
import com.magic.longvideo.pojo.entity.*;
import com.magic.longvideo.pojo.vo.VideoInfoVo;
import com.magic.longvideo.service.user.UserVideoService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserVideoServiceImpl implements UserVideoService {

    private static final String USER_VIDEO_MAP_PREFIX = "USER_VIDEO_MAP_";
    private static final String WATCH_PARAMS = "WATCH_PARAMS";
    //TODO 解决进度以及时间问题
    //private static final String USER_VIDEO_WATCH_RECORD_PREFIX = "USER_VIDEO_WATCH_RECORD_PREFIX_";

    private final LongVideoUserHistoryMapper longVideoUserHistoryMapper;
    private final LongVideoUserFavoriteMapper longVideoUserFavoriteMapper;
    private final LongVideoUserPurchasedMapper longVideoUserPurchasedMapper;
    private final LongVideoUserCoinDetailsMapper longVideoUserCoinDetailsMapper;
    private final LongVideoUserMapper longVideoUserMapper;


    public UserVideoServiceImpl(
            LongVideoUserHistoryMapper longVideoUserHistoryMapper,
            LongVideoUserFavoriteMapper longVideoUserFavoriteMapper,
            LongVideoUserPurchasedMapper longVideoUserPurchasedMapper,
            LongVideoUserMapper longVideoUserMapper, LongVideoUserCoinDetailsMapper longVideoUserCoinDetailsMapper) {
        this.longVideoUserFavoriteMapper = longVideoUserFavoriteMapper;
        this.longVideoUserPurchasedMapper = longVideoUserPurchasedMapper;
        this.longVideoUserHistoryMapper = longVideoUserHistoryMapper;
        this.longVideoUserMapper = longVideoUserMapper;
        this.longVideoUserCoinDetailsMapper = longVideoUserCoinDetailsMapper;
    }

    @Override
    public List<VideoInfoDto> getVideoInfoFromUserVideoMapByFieldName(Long userId, UserVideoMapFieldName fieldName) {
        String videoInfosJson = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString());
        String videoWatchParams = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, WATCH_PARAMS);
        if (StrUtil.isBlank(videoInfosJson)) {
            QueryWrapper<LongVideoUserHistoryEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", userId);
            LongVideoUserHistoryEntity entity = longVideoUserHistoryMapper.selectOne(queryWrapper);
            if (entity != null) {
                videoInfosJson = entity.getVideoHistoryInfos();
                JedisUtil.hSet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString(), videoInfosJson);
            }
        }
        JSONArray videoInfosJsonArray = JSONUtil.parseArray(videoInfosJson);
        JSONObject watchParams = JSONUtil.parseObj(videoWatchParams);
        for (JSONObject jsonObject : videoInfosJsonArray.jsonIter()) {
            String videoId = jsonObject.getStr("videoId");
            JSONObject watchParam = watchParams.getJSONObject(videoId);
            jsonObject.set("watchDate", watchParam.getLong("watchDate")).set("watchProgress", watchParam.getLong("watchProgress"));
        }
        videoInfosJsonArray.sort((o1, o2) -> {
            JSONObject preVideo = JSONUtil.parseObj(o1);
            JSONObject afterVideo = JSONUtil.parseObj(o2);
            return (int) (preVideo.getLong("watchDate") - afterVideo.getLong("watchDate"));
        });
        List<VideoInfoDto> videoInfoDtoList = JSONUtil.toList(videoInfosJsonArray, VideoInfoDto.class);
        for (VideoInfoDto videoInfoDto : videoInfoDtoList) {
            videoInfoDto.setWatchProgress((videoInfoDto.getWatchProgress() / videoInfoDto.getVideoDuration()) * 100);
        }

        return videoInfoDtoList;
    }

    @Override
    public boolean addVideoInfoToUserVideoMapByFieldName(Long userId, VideoInfoVo videoInfoVo, UserVideoMapFieldName fieldName) {
        String videoInfosJson = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString());
        String videoWatchParams = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, WATCH_PARAMS);

        JSONObject watchParams = JSONUtil.parseObj(videoWatchParams);
        JSONObject videoWatchParam = watchParams.getJSONObject(videoInfoVo.getVideoId().toString());
        if (videoWatchParam == null) {
            videoWatchParam = new JSONObject();
            watchParams.set(videoInfoVo.getVideoId().toString(), videoWatchParam);
        }
        JSONArray videoInfosJsonArray = JSONUtil.parseArray(videoInfosJson);

        List<VideoInfoDto> videoInfosList = JSONUtil.toList(videoInfosJsonArray, VideoInfoDto.class);
        List<VideoInfoDto> newHistoryVideoInfos = removeVideoInfoFromJsonById(videoInfosList, watchParams, videoInfoVo.getVideoId());
        VideoInfoDto videoInfoDto = new VideoInfoDto();
        BeanUtil.copyProperties(videoInfoVo, videoInfoDto);
        videoWatchParam.set("watchDate", videoInfoVo.getWatchDate());
        videoWatchParam.set("watchProgress", videoInfoVo.getWatchProgress());
        newHistoryVideoInfos.add(videoInfoDto);
        String videoInfos = JSONUtil.toJsonStr(newHistoryVideoInfos);
        boolean isAdd = JedisUtil.hSet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString(), videoInfos);
        boolean watchParamsIsAdd = JedisUtil.hSet(USER_VIDEO_MAP_PREFIX + userId, WATCH_PARAMS, watchParams.toString());

        updateLongVideoUserWatchHistory(userId, videoInfos, fieldName);
        return watchParamsIsAdd == isAdd;
    }

    @Override
    public boolean removeVideoInfoFromUserVideoMapByFieldName(Long userId, List<Long> videoIds, UserVideoMapFieldName fieldName) {
        String videoInfosJson = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString());
        String videoWatchParams = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, WATCH_PARAMS);

        JSONObject watchParams = JSONUtil.parseObj(videoWatchParams);
        JSONArray jsonArray = JSONUtil.parseArray(videoInfosJson);

        List<VideoInfoDto> videosInfoList = JSONUtil.toList(jsonArray, VideoInfoDto.class);
        List newHistoryVideoInfos = removeVideoInfoFromJsonByIds(videosInfoList, watchParams, videoIds);
        String videosInfo = JSONUtil.toJsonStr(newHistoryVideoInfos);
        boolean watchParamsIsRemove = JedisUtil.hSet(USER_VIDEO_MAP_PREFIX + userId, WATCH_PARAMS, watchParams.toString());
        boolean isRemove = JedisUtil.hSet(USER_VIDEO_MAP_PREFIX + userId, fieldName.toString(), videosInfo);

        updateLongVideoUserWatchHistory(userId, videosInfo, fieldName);
        return isRemove == watchParamsIsRemove;
    }


    public List<VideoInfoDto> removeVideoInfoFromJsonByIds(List<VideoInfoDto> videosInfoList, JSONObject watchParams, List<Long> videoIds) {
        for (int i = 0; i < videosInfoList.size(); i++) {
            VideoInfoDto videoInfoDto = videosInfoList.get(i);
            if (videoIds.contains(videoInfoDto.getVideoId())) {
                videosInfoList.remove(i--);
                watchParams.remove(videoInfoDto.getVideoId());
            }
        }
        return videosInfoList;
    }

    public List<VideoInfoDto> removeVideoInfoFromJsonById(List<VideoInfoDto> videosInfoList, JSONObject watchParams, Long videoId) {
        for (int i = 0; i < videosInfoList.size(); i++) {
            VideoInfoDto videoInfoDto = videosInfoList.get(i);
            if (videoId.equals(videoInfoDto.getVideoId())) {
                videosInfoList.remove(i);
                watchParams.remove(videoInfoDto.getVideoId());
                break;
            }
        }
        return videosInfoList;
    }


    private static final String VIDEO_IS_PRAISE_PREFIX = " VIDEO_IS_PRAISE_";

    @Override
    public VideoPraiseDto praise(Integer videoId, Long userId) {
        String videoIsPraiseJson = JedisUtil.getStr(VIDEO_IS_PRAISE_PREFIX + videoId);
        JSONArray userIds = JSONUtil.parseArray(videoIsPraiseJson);
        VideoPraiseDto videoPraiseDto = new VideoPraiseDto();
        if (!userIds.contains(String.valueOf(userId))) {
            userIds.add(String.valueOf(userId));
            videoPraiseDto.setPraise(true);
        } else {
            userIds.remove(String.valueOf(userId));
            videoPraiseDto.setPraise(false);
        }
        if (!JedisUtil.setStr(VIDEO_IS_PRAISE_PREFIX + videoId, JSONUtil.toJsonStr(userIds))) {
            videoPraiseDto.setPraise(false);
        }
        return videoPraiseDto;
    }


    //TODO 可做异步
    public int updateLongVideoUserWatchHistory(Long userId, String videoInfos, UserVideoMapFieldName fieldName) {
        if (UserVideoMapFieldName.HISTORY == fieldName) {
            LongVideoUserHistoryEntity entity = new LongVideoUserHistoryEntity();
            entity.setUserId(userId).setVideoHistoryInfos(videoInfos);
            return longVideoUserHistoryMapper.insertOnDuplicateKeyUpdate(entity);
        } else if (UserVideoMapFieldName.PURCHASED == fieldName) {
            LongVideoUserPurchasedEntity entity = new LongVideoUserPurchasedEntity();
            entity.setUserId(userId).setVideoPurchasedInfos(videoInfos);
            return longVideoUserPurchasedMapper.insertOnDuplicateKeyUpdate(entity);
        } else if (UserVideoMapFieldName.FAVORITE == fieldName) {
            LongVideoUserFavoriteEntity entity = new LongVideoUserFavoriteEntity();
            entity.setUserId(userId).setVideoFavoriteInfos(videoInfos);
            return longVideoUserFavoriteMapper.insertOnDuplicateKeyUpdate(entity);
        }
        return 0;
    }

    private static final String DAILY_PREFIX = "DAILY_";
    private static final String WEEKLY_PREFIX = "WEEKLY_";
    private static final String MONTHLY_PREFIX = "MONTHLY_";

    @Override
    public void increaseVideoRankListScore(Object obj, int videoRankTypeKey) {
        String cacheKey = VideoRankType.getValueByKey(videoRankTypeKey);
        JSONObject jsonObject = JSONUtil.parseObj(obj);
        jsonObject.remove("watchProgress");
        jsonObject.remove("watchDate");

        Date today = new Date();

        JedisUtil.zIncrBy(DAILY_PREFIX + cacheKey, 1, JSONUtil.toJsonStr(jsonObject));
        DateTime dateTimeOfEndDay = MagicDateUtil.endOfDay(today);
        int endOfDaySecond = (int) ((dateTimeOfEndDay.getTime() - today.getTime()) / 1000);
        JedisUtil.expireNotExist(DAILY_PREFIX + cacheKey, endOfDaySecond);

        JedisUtil.zIncrBy(WEEKLY_PREFIX + cacheKey, 1, JSONUtil.toJsonStr(jsonObject));
        DateTime dateTimeOfEndWeek = MagicDateUtil.endOfWeek(today);
        int endOfWeekSecond = (int) ((dateTimeOfEndWeek.getTime() - today.getTime()) / 1000);
        JedisUtil.expireNotExist(WEEKLY_PREFIX + cacheKey, endOfWeekSecond);

        JedisUtil.zIncrBy(MONTHLY_PREFIX + cacheKey, 1, JSONUtil.toJsonStr(jsonObject));
        DateTime dateTimeOfEndMonth = MagicDateUtil.endOfMonth(today);
        int endOfMonthSecond = (int) ((dateTimeOfEndMonth.getTime() - today.getTime()) / 1000);
        JedisUtil.expireNotExist(MONTHLY_PREFIX + cacheKey, endOfMonthSecond);

        //TODO 异步long_video_video_info排行榜中的数据增1
    }

    @Override
    public void updateUserCoinDetails(Long userId, String videoTitle, Integer videoPayCoin) {
        LongVideoUserEntity longVideoUserEntity = longVideoUserMapper.selectById(userId);
        LongVideoUserCoinDetailsEntity longVideoUserCoinDetailsEntity = longVideoUserCoinDetailsMapper.selectById(userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.set("balance", longVideoUserEntity.getGoldCoins());
        jsonObject.set("deducted", "-" + videoPayCoin);
        jsonObject.set("videoTitle", videoTitle);
        jsonObject.set("purchaseDate", System.currentTimeMillis());
        if (longVideoUserCoinDetailsEntity == null) {
            longVideoUserCoinDetailsEntity = new LongVideoUserCoinDetailsEntity();
            longVideoUserCoinDetailsEntity.setUserId(userId);
            longVideoUserCoinDetailsEntity.setCoinDetails(new JSONArray().put(jsonObject).toString());
            longVideoUserCoinDetailsMapper.insert(longVideoUserCoinDetailsEntity);
        } else {
            JSONArray jsonArray = JSONUtil.parseArray(longVideoUserCoinDetailsEntity.getCoinDetails());
            jsonArray.put(jsonObject);
            longVideoUserCoinDetailsEntity.setCoinDetails(jsonArray.toString());
            longVideoUserCoinDetailsMapper.updateById(longVideoUserCoinDetailsEntity);
        }

    }

}
