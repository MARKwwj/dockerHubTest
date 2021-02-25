package com.magic.video.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.magic.framework.redis.JedisUtil;
import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.framework.utils.MagicUtil;
import com.magic.framework.utils.TokenUtil;
import com.magic.video.common.consts.VideoFilterConstant;
import com.magic.video.common.consts.VideoStatus;
import com.magic.video.manager.rmi.SearchServiceRmi;
import com.magic.video.mapper.*;
import com.magic.video.pojo.dto.*;
import com.magic.video.pojo.entity.LongVideoClassifyInfoEntity;
import com.magic.video.pojo.entity.LongVideoCollectionRelationEntity;
import com.magic.video.pojo.entity.LongVideoTagRelationEntity;
import com.magic.video.pojo.entity.LongVideoVideoInfoEntity;
import com.magic.video.pojo.vo.VideoCollectionVo;
import com.magic.video.pojo.vo.VideoFilterParamsVo;
import com.magic.video.pojo.vo.VideoSearchVo;
import com.magic.video.service.VideoService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {

    private static final String VIDEO_IS_PRAISE_PREFIX = " VIDEO_IS_PRAISE_";

    private final LongVideoCollectionRelationMapper longVideoCollectionRelationMapper;
    private final LongVideoVideoInfoMapper longVideoVideoInfoMapper;
    private final LongVideoClassifyRelationMapper longVideoClassifyRelationMapper;
    private final LongVideoCategoryRelationMapper longVideoCategoryRelationMapper;
    private final LongVideoClassifyInfoMapper longVideoClassifyInfoMapper;
    private final LongVideoTagRelationMapper longVideoTagRelationMapper;
    private final SearchServiceRmi searchServiceRmi;

    public VideoServiceImpl(
            LongVideoCollectionRelationMapper longVideoCollectionRelationMapper,
            LongVideoVideoInfoMapper longVideoVideoInfoMapper,
            LongVideoClassifyRelationMapper longVideoClassifyRelationMapper,
            LongVideoCategoryRelationMapper longVideoCategoryRelationMapper,
            LongVideoClassifyInfoMapper longVideoClassifyInfoMapper,
            LongVideoTagRelationMapper longVideoTagRelationMapper,
            SearchServiceRmi searchServiceRmi

    ) {
        this.searchServiceRmi = searchServiceRmi;
        this.longVideoTagRelationMapper = longVideoTagRelationMapper;
        this.longVideoClassifyInfoMapper = longVideoClassifyInfoMapper;
        this.longVideoCategoryRelationMapper = longVideoCategoryRelationMapper;
        this.longVideoClassifyRelationMapper = longVideoClassifyRelationMapper;
        this.longVideoVideoInfoMapper = longVideoVideoInfoMapper;
        this.longVideoCollectionRelationMapper = longVideoCollectionRelationMapper;
    }


    @Override
    public LongVideoInfoDto info(Integer videoId) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        LongVideoVideoInfoEntity longVideoVideoInfoEntity = longVideoVideoInfoMapper.selectById(videoId);
        if (longVideoVideoInfoEntity == null) {
            return null;
        }

        LongVideoInfoDto longVideoInfoDto = new LongVideoInfoDto();
        BeanUtil.copyProperties(longVideoVideoInfoEntity, longVideoInfoDto, "videoTags", "videoPlayCount");
        Integer videoDuration = longVideoVideoInfoEntity.getVideoDuration();
        String hoursMinuteSecond = MagicUtil.hoursMinuteSecond(videoDuration);
        longVideoInfoDto.setHms(hoursMinuteSecond);

        longVideoInfoDto.setVideoPlayCount(MagicUtil.numberConvertToStr(longVideoVideoInfoEntity.getVideoPlayCount(), 2));
        String videoTags = longVideoVideoInfoEntity.getVideoTags();
        JSONArray jsonArray = JSONUtil.parseArray(videoTags);
        List<JSONObject> videoTagsList = new ArrayList<>();
        for (JSONObject jsonObject : jsonArray.jsonIter()) {
            videoTagsList.add(jsonObject);
        }
        //判断视频限免是否过期
        if (longVideoVideoInfoEntity.getVideoPayType().equals(2)) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(1970, 0, 1);
            if (DateUtil.isIn(new Date(), calendar.getTime(), longVideoVideoInfoEntity.getVideoFreeExpires())) {
                longVideoInfoDto.setVideoPayType(0);
            } else {
                longVideoInfoDto.setVideoPayType(3);
            }
        }
        longVideoInfoDto.setVideoTags(videoTagsList);
        //判断用户是否点赞视频
        String videoIsPraiseJson = JedisUtil.getStr(VIDEO_IS_PRAISE_PREFIX + longVideoVideoInfoEntity.getVideoId());
        JSONArray userIds = JSONUtil.parseArray(videoIsPraiseJson);
        if (userIds.contains(String.valueOf(userId))) {
            longVideoInfoDto.setPraise(true);
        }
        //判断用户是否收藏视频
        String videoInfosJson = JedisUtil.hGet("USER_VIDEO_MAP_" + userId, "FAVORITE");
        JSONArray videoInfosJsonArray = JSONUtil.parseArray(videoInfosJson);
        for (JSONObject jsonObject : videoInfosJsonArray.jsonIter()) {
            if (String.valueOf(videoId).equals(jsonObject.getStr("videoId"))) {
                longVideoInfoDto.setFavorite(true);
                break;
            }
        }
        //计算用户观看此视频的上次观看时间
        videoInfosJson = JedisUtil.hGet("USER_VIDEO_MAP_" + userId, "HISTORY");
        videoInfosJsonArray = JSONUtil.parseArray(videoInfosJson);
        for (JSONObject jsonObject : videoInfosJsonArray.jsonIter()) {
            if (String.valueOf(videoId).equals(jsonObject.getStr("videoId"))) {
                Integer watchPercent = jsonObject.getInt("watchProgress");
                longVideoInfoDto.setLastWatchedTime(watchPercent);
                break;
            }
        }
        //TODO 异步更新视频播放数
        return longVideoInfoDto;
    }


    @Override
    public Map<String, Object> collection(VideoCollectionVo videoCollectionVo) {
        QueryWrapper<LongVideoCollectionRelationEntity> longVideoCollectionRelationEntityQueryWrapper = new QueryWrapper<>();
        longVideoCollectionRelationEntityQueryWrapper.select("video_id");
        longVideoCollectionRelationEntityQueryWrapper.eq("collection_id", videoCollectionVo.getCollectionId());
        Page<LongVideoCollectionRelationEntity> page = new Page<>(videoCollectionVo.getPageNum(), videoCollectionVo.getPageSize());
        longVideoCollectionRelationMapper.selectPage(page, longVideoCollectionRelationEntityQueryWrapper);
        List<LongVideoCollectionRelationEntity> longVideoCollectionInfoEntityList = page.getRecords();
        List<Integer> collectionIds = longVideoCollectionInfoEntityList.stream().map(
                LongVideoCollectionRelationEntity::getVideoId
        ).collect(Collectors.toList());
        List<LongVideoCollectionDto> longVideoCollectionDtoList = Collections.emptyList();
        if (collectionIds.size() != 0) {
            QueryWrapper<LongVideoVideoInfoEntity> longVideoVideoInfoEntityQueryWrapper = new QueryWrapper<>();
            longVideoVideoInfoEntityQueryWrapper.in("video_id", collectionIds);
            longVideoVideoInfoEntityQueryWrapper.eq("video_status", VideoStatus.ENABLE);
            List<LongVideoVideoInfoEntity> longVideoVideoInfoEntityList = longVideoVideoInfoMapper.selectList(longVideoVideoInfoEntityQueryWrapper);
            longVideoCollectionDtoList = MagicBeanUtil.copyListProperties(longVideoVideoInfoEntityList, LongVideoCollectionDto::new, (source, target) -> {
                //判断视频限免是否过期
                if (source.getVideoPayType().equals(2)) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(1970, 1, 1);
                    if (DateUtil.isIn(new Date(), calendar.getTime(), source.getVideoFreeExpires())) {
                        target.setVideoPayType(0);
                    } else {
                        target.setVideoPayType(3);
                    }
                }
                Integer videoDuration = source.getVideoDuration();
                String hoursMinuteSecond = MagicUtil.hoursMinuteSecond(videoDuration);
                target.setHms(hoursMinuteSecond);
            });
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("totalPage", PageUtil.totalPage((int) page.getTotal(), videoCollectionVo.getPageSize()));
        result.put("videoCollectionList", longVideoCollectionDtoList);
        return result;
    }

    @Override
    public Map<String, Object> filter(VideoFilterParamsVo videoFilterParamsVo) {
        QueryWrapper<LongVideoVideoInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("video_status", VideoStatus.ENABLE);
        Integer orderBy = videoFilterParamsVo.getOrderBy();
        Integer classifyId = videoFilterParamsVo.getClassifyId();
        Integer duration = videoFilterParamsVo.getDuration();
        Integer videoPayType = videoFilterParamsVo.getVideoPayType();
        Integer categoryId = videoFilterParamsVo.getCategoryId();
        Integer curPage = videoFilterParamsVo.getPageNum();
        Integer pageSize = videoFilterParamsVo.getPageSize();
        if (orderBy != null && orderBy != VideoFilterConstant.ORDER_BY_COMPREHENSIVE_SORT) {
            String orderByColumn;
            switch (orderBy) {
                case VideoFilterConstant.ORDER_BY_LATEST_UPDATE:
                    orderByColumn = "video_update_time";
                    break;
                case VideoFilterConstant.ORDER_BY_MOST_PLAYED:
                    orderByColumn = "video_play_count";
                    break;
                case VideoFilterConstant.ORDER_BY_MOST_SCORE:
                    orderByColumn = "video_score";
                    break;
                case VideoFilterConstant.ORDER_BY_MOST_FAVOURITE:
                    orderByColumn = "video_favorite_count";
                    break;
                default:
                    orderByColumn = "video_id";
                    break;
            }
            queryWrapper.orderByDesc(orderByColumn);
        }
        if (duration != null && duration != VideoFilterConstant.DURATION_ALL) {
            int minDuration;
            int maxDuration;
            switch (duration) {
                case VideoFilterConstant.DURATION_BETWEEN_ZERO_TO_THIRTY:
                    minDuration = 0;
                    maxDuration = 30 * 60;
                    break;
                case VideoFilterConstant.DURATION_BETWEEN_THIRTY_TO_SIXTY:
                    minDuration = 30 * 60;
                    maxDuration = 60 * 60;
                    break;
                case VideoFilterConstant.DURATION_BETWEEN_SIXTY_TO_ONE_HUNDRED_TWENTY:
                    minDuration = 60 * 60;
                    maxDuration = 120 * 60;
                    break;
                case VideoFilterConstant.DURATION_OVER_ONE_HUNDRED_TWENTY:
                    minDuration = 120 * 60;
                    maxDuration = Integer.MAX_VALUE;
                    break;
                default:
                    minDuration = 0;
                    maxDuration = Integer.MAX_VALUE;
                    break;
            }
            queryWrapper.between("video_duration", minDuration, maxDuration);
        }
        if (videoPayType != null && videoPayType != VideoFilterConstant.PAY_TYPE_BY_ALL) {
            if (videoPayType == VideoFilterConstant.PAY_TYPE_BY_FREE) {
                queryWrapper.eq("video_pay_type", 0).or().eq("video_pay_type", 2);
            } else if (videoPayType == VideoFilterConstant.PAY_TYPE_BY_COINS_OR_VIP) {
                queryWrapper.eq("video_pay_type", 3).or().eq("video_pay_type", 1);
            }
        }
        Integer[] videoIds;
        if (!classifyId.equals(0)) {
            videoIds = longVideoClassifyRelationMapper.selectVideoIdsByClassifyId(classifyId);
        } else {
            videoIds = longVideoCategoryRelationMapper.selectVideoIdsByCategoryId(categoryId);
        }
        int totalPage = 0;
        List<VideoFilterInfoDto> videoFilterInfoDtoList = new ArrayList<>();
        if (videoIds != null && videoIds.length != 0) {
            //将合集查出的视频Id进行分页操作
            PageUtil.setFirstPageNo(1);
            totalPage = PageUtil.totalPage(curPage, pageSize);
            int startIndex = PageUtil.getStart(curPage, pageSize);
            int endIndex = PageUtil.getEnd(curPage, pageSize);
            videoIds = Arrays.copyOfRange(videoIds, startIndex, endIndex);
            if (videoIds.length != 0) {
                queryWrapper.in("video_id", videoIds);
            }
            List<LongVideoVideoInfoEntity> longVideoVideoInfoEntityList = longVideoVideoInfoMapper.selectList(queryWrapper);
            videoFilterInfoDtoList = MagicBeanUtil.copyListProperties(longVideoVideoInfoEntityList, VideoFilterInfoDto::new, (source, target) -> {
                String tagsJson = source.getVideoTags();
                if (StrUtil.isNotBlank(tagsJson)) {
                    JSONArray jsonArray = JSONUtil.parseArray(tagsJson);
                    List<JSONObject> tags = new ArrayList<>(jsonArray.size());
                    for (JSONObject jsonObject : jsonArray.jsonIter()) {
                        jsonObject.remove("tagId");
                        tags.add(jsonObject);
                    }
                    target.setVideoTagsList(tags);
                }
            });
        }
        Map<String, Object> result = new HashMap<>(2);
        result.put("totalPage", totalPage);
        result.put("videoFilterList", videoFilterInfoDtoList);
        return result;
    }

    @Override
    public List<LongVideoClassifyInfoDto> classify(Integer categoryId) {
        QueryWrapper<LongVideoClassifyInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id", categoryId);
        queryWrapper.orderByAsc("classify_sort");
        List<LongVideoClassifyInfoEntity> classifyInfoEntityList = longVideoClassifyInfoMapper.selectList(queryWrapper);
        List<LongVideoClassifyInfoDto> longVideoClassifyInfoDtoList = MagicBeanUtil.copyListProperties(classifyInfoEntityList, LongVideoClassifyInfoDto::new, (source, target) -> {
            target.setId(source.getClassifyId());
            target.setName(source.getClassifyName());
        });
        longVideoClassifyInfoDtoList.add(0, new LongVideoClassifyInfoDto().setId(0).setName("全部"));
        return longVideoClassifyInfoDtoList;
    }

    @Override
    public Map<String, Object> newest(NewestVideoVo newestVideoVo) {
        Page<LongVideoVideoInfoEntity> page = new Page<>(newestVideoVo.getPageNum(), newestVideoVo.getPageSize());
        QueryWrapper<LongVideoVideoInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("video_update_time");
        queryWrapper.eq("video_status", VideoStatus.ENABLE);
        Page<LongVideoVideoInfoEntity> longVideoVideoInfoEntityPage = longVideoVideoInfoMapper.selectPage(page, queryWrapper);
        Map<String, Object> result = new HashMap<>(2);
        result.put("totalPage", longVideoVideoInfoEntityPage.getTotal());
        result.put("videoList", MagicBeanUtil.copyListProperties(longVideoVideoInfoEntityPage.getRecords(), NewestVideoDto::new));
        return result;
    }

    /**
     * long-video-server服务中通过这个key在redis存储了观看历史，
     * 可以将历史记录取出然后获取对应的标签进行推荐
     */
    private static final String USER_VIDEO_MAP_PREFIX = "USER_VIDEO_MAP_";

    @Override
    public JsonResult recommend(VideoSearchVo videoSearchVo, Long userId) {
        if (videoSearchVo.getVideoId() != null) {
            QueryWrapper<LongVideoTagRelationEntity> queryWrapper = new QueryWrapper();
            queryWrapper.in("video_id", videoSearchVo.getVideoId());
            List<String> tagNameList = longVideoTagRelationMapper.selectTagIdByVideoId(queryWrapper);
            StringBuilder keywordsBuilder = new StringBuilder();
            for (String tagName : tagNameList) {
                keywordsBuilder.append(tagName);
            }
            videoSearchVo.setKeywords(keywordsBuilder.toString());
        } else {
            String videoInfosJson = JedisUtil.hGet(USER_VIDEO_MAP_PREFIX + userId, "HISTORY");
            if (StrUtil.isBlank(videoInfosJson)) {
                return JsonResult.success();
            }
            JSONArray jsonArray = JSONUtil.parseArray(videoInfosJson);
            List<Integer> videoId = new ArrayList<>();
            for (JSONObject jsonObject : jsonArray.jsonIter()) {
                videoId.add(jsonObject.getInt("videoId"));
            }
            QueryWrapper<LongVideoTagRelationEntity> queryWrapper = new QueryWrapper();
            if (videoId != null && videoId.size() != 0) {
                queryWrapper.in("video_id", videoId);
            }
            List<String> tagNameList = longVideoTagRelationMapper.selectTagIdByVideoId(queryWrapper);
            StringBuilder keywordsBuilder = new StringBuilder();
            for (String tagName : tagNameList) {
                keywordsBuilder.append(tagName);
            }
            videoSearchVo.setKeywords(keywordsBuilder.toString());
        }
        return searchServiceRmi.search(videoSearchVo);
    }


}
