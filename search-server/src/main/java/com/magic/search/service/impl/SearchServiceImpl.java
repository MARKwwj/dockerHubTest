package com.magic.search.service.impl;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.tokenizer.Result;
import cn.hutool.extra.tokenizer.TokenizerEngine;
import cn.hutool.extra.tokenizer.TokenizerUtil;
import cn.hutool.extra.tokenizer.Word;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.framework.utils.MagicUtil;
import com.magic.search.mapper.LongVideoCategoryRelationMapper;
import com.magic.search.mapper.LongVideoVideoInfoMapper;
import com.magic.search.pojo.dto.SearchResultDto;
import com.magic.search.pojo.dto.VideoIdSort;
import com.magic.search.pojo.entity.LongVideoCategoryRelationEntity;
import com.magic.search.pojo.entity.LongVideoVideoInfoEntity;
import com.magic.search.pojo.vo.VideoSearchVo;
import com.magic.search.service.SearchService;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@EnableScheduling
public class SearchServiceImpl implements SearchService {


    private Map<String, List<Integer>> tagVideoMaps;
    private Map<String, List<Integer>> pinYinTitleVideoMaps;
    private Map<String, List<Integer>> textTitleVideoMaps;
    private Map<Integer, List<Integer>> categoryVideoMaps;
    private Map<String, String> tagmap;




    private final LongVideoVideoInfoMapper longVideoVideoInfoMapper;

    private final LongVideoCategoryRelationMapper longVideoCategoryRelationMapper;

    public SearchServiceImpl(
            LongVideoVideoInfoMapper longVideoVideoInfoMapper,
            LongVideoCategoryRelationMapper longVideoCategoryRelationMapper
    ) {
        this.longVideoCategoryRelationMapper = longVideoCategoryRelationMapper;
        this.longVideoVideoInfoMapper = longVideoVideoInfoMapper;
    }

    private void computedVideoWeight(List<Integer> videoIds, HashMap<Integer, Integer> videoIdScoreMap) {
        if (videoIds != null) {
            for (Integer videoId : videoIds) {
                if (videoIdScoreMap.containsKey(videoId)) {
                    videoIdScoreMap.put(videoId, videoIdScoreMap.get(videoId) + 1);
                } else {
                    videoIdScoreMap.put(videoId, 1);
                }
            }
        }
    }


    @Override
    public HashMap<String, Object> search(VideoSearchVo videoSearchVo) {
        TokenizerEngine engine = TokenizerUtil.createEngine();
        Result words = engine.parse(videoSearchVo.getKeywords());
        HashMap<Integer, Integer> videoIdScoreMap = new LinkedHashMap<>(5000);
        for (Word word : words) {
            String text = word.getText();
            String pinYin = getPingYin(text);
            List<Integer> videoTagIds = tagVideoMaps.get(pinYin);
            computedVideoWeight(videoTagIds, videoIdScoreMap);
            List<Integer> videoPinYinTitleIds = pinYinTitleVideoMaps.get(pinYin);
            computedVideoWeight(videoPinYinTitleIds, videoIdScoreMap);
            List<Integer> videoTextTitleIds = textTitleVideoMaps.get(text);
            computedVideoWeight(videoTextTitleIds, videoIdScoreMap);
        }
        //排除合集外的视频
        Integer categoryId = videoSearchVo.getCategoryId();
        if (categoryId != null && !categoryId.equals(0)) {
            HashMap<Integer, Integer> videoIdScoreTempMap = videoIdScoreMap;
            videoIdScoreMap = new HashMap<>(5000);
            List<Integer> videoIds = categoryVideoMaps.get(categoryId);
            if (videoIds != null) {
                for (Integer videoId : videoIds) {
                    Integer score = videoIdScoreTempMap.get(videoId);
                    if (score != null) {
                        videoIdScoreMap.put(videoId, score);
                    }
                }
            }
        }
        TreeSet<VideoIdSort> videoIdSorts = new TreeSet<>();
        for (Map.Entry<Integer, Integer> entry : videoIdScoreMap.entrySet()) {
            videoIdSorts.add(new VideoIdSort().setVideoId(entry.getKey()).setSort(entry.getValue()));
        }
        ArrayList<VideoIdSort> videoIdSortArrayList = new ArrayList(videoIdSorts);
        Integer curPage = videoSearchVo.getPageNum();
        Integer pageSize = videoSearchVo.getPageSize();
        int totalPage = 0;
        ArrayList<LongVideoVideoInfoEntity> longVideoVideoInfoEntitySortList = new ArrayList<>();
        List<SearchResultDto> searchResultDtoList = Collections.emptyList();
        if (videoIdSortArrayList.size() != 0) {
            //将合集查出的视频Id进行分页操作
            PageUtil.setFirstPageNo(1);
            totalPage = PageUtil.totalPage(videoIdSortArrayList.size(), pageSize);
            if (curPage <= totalPage) {
                int startIndex = PageUtil.getStart(curPage, pageSize);
                int endIndex = PageUtil.getEnd(curPage, pageSize);
                endIndex = endIndex > videoIdSortArrayList.size() ? videoIdSortArrayList.size() : endIndex;
                List<VideoIdSort> videoIdSortList = videoIdSortArrayList.subList(startIndex, endIndex);
                List<Integer> videoIdList = new ArrayList<>();
                for (VideoIdSort videoIdSort : videoIdSortList) {
                    videoIdList.add(videoIdSort.getVideoId());
                }
                QueryWrapper<LongVideoVideoInfoEntity> queryWrapper = null;
                if (videoIdList.size() != 0) {
                    queryWrapper = new QueryWrapper<>();
                    queryWrapper.in("video_id", videoIdList);
                }
                List<LongVideoVideoInfoEntity> longVideoVideoInfoEntityList = longVideoVideoInfoMapper.selectList(queryWrapper);
                LinkedList<LongVideoVideoInfoEntity> longVideoVideoInfoEntityLinkedList = new LinkedList<>(longVideoVideoInfoEntityList);
                for (Integer videoId : videoIdList) {
                    for (LongVideoVideoInfoEntity entity : longVideoVideoInfoEntityLinkedList) {
                        if (entity.getVideoId().equals(videoId)) {
                            longVideoVideoInfoEntitySortList.add(entity);
                            longVideoVideoInfoEntityLinkedList.remove(entity);
                            break;
                        }
                    }
                }
            }
            searchResultDtoList = MagicBeanUtil.copyListProperties(longVideoVideoInfoEntitySortList, SearchResultDto::new, (source, target) -> {
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
                String hms = MagicUtil.hoursMinuteSecond(source.getVideoDuration());
                target.setHms(hms);
                target.setVideoPlayCount(MagicUtil.numberConvertToStr(source.getVideoPlayCount(), 2));
            }, "videoPlayCount");
        }
        HashMap<String, Object> result = new HashMap<>(2);
        result.put("totalPage", totalPage);
        result.put("videoSearchList", searchResultDtoList);
        return result;
    }

    @Override
    @PostConstruct
    @Scheduled(cron = "0 0,10,20,30,40,50 * * * ?")
    public void refreshVideoMaps() {
        System.out.println("初始化");
        tagVideoMaps = new HashMap<>(5000);
        pinYinTitleVideoMaps = new HashMap<>(5000);
        textTitleVideoMaps = new HashMap<>(5000);
        categoryVideoMaps = new HashMap<>(5000);
        tagmap = new HashMap<>(5000);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select("video_id", "video_title", "video_tags");
        queryWrapper.orderByDesc("video_play_count");
        //初始化视频标题和标签的索引
        List<LongVideoVideoInfoEntity> longVideoVideoInfoEntityList = longVideoVideoInfoMapper.selectList(queryWrapper);
        TokenizerEngine engine = TokenizerUtil.createEngine();
        for (LongVideoVideoInfoEntity longVideoVideoInfoEntity : longVideoVideoInfoEntityList) {
            Result title = engine.parse(longVideoVideoInfoEntity.getVideoTitle());
            List<Integer> videoIdList;
            for (Word word : title) {
                String keywords = word.getText();
                //筛选汉字
                videoIdList = textTitleVideoMaps.get(keywords);
                if (videoIdList == null) {
                    videoIdList = new ArrayList<>();
                }
                videoIdList.add(longVideoVideoInfoEntity.getVideoId());
                textTitleVideoMaps.put(keywords, videoIdList);
                //筛选拼音
                String pinYin = getPingYin(keywords);
                videoIdList = pinYinTitleVideoMaps.get(pinYin);
                if (videoIdList == null) {
                    videoIdList = new ArrayList<>();
                }
                videoIdList.add(longVideoVideoInfoEntity.getVideoId());
                pinYinTitleVideoMaps.put(pinYin, videoIdList);
            }
            JSONArray jsonArray = JSONUtil.parseArray(longVideoVideoInfoEntity.getVideoTags());
            for (JSONObject jsonObject : jsonArray.jsonIter()) {
                String tagName = jsonObject.getStr("tagName");
                Result tagNameWords = engine.parse(tagName);
                for (Word tagNameWord : tagNameWords) {
                    String keywords = tagNameWord.getText();
                    videoIdList = textTitleVideoMaps.get(keywords);
                    if (videoIdList == null) {
                        videoIdList = new ArrayList<>();
                    }
                    videoIdList.add(longVideoVideoInfoEntity.getVideoId());
                    textTitleVideoMaps.put(keywords, videoIdList);

                    String pingYin = getPingYin(keywords);
                    videoIdList = pinYinTitleVideoMaps.get(pingYin);
                    if (videoIdList == null) {
                        videoIdList = new ArrayList<>();
                    }
                    videoIdList.add(longVideoVideoInfoEntity.getVideoId());
                    pinYinTitleVideoMaps.put(pingYin, videoIdList);
                }
            }
        }
        //初始化类别的索引
        List<LongVideoCategoryRelationEntity> longVideoCategoryRelationEntityList = longVideoCategoryRelationMapper.selectList(null);
        for (LongVideoCategoryRelationEntity longVideoCategoryRelationEntity : longVideoCategoryRelationEntityList) {
            List<Integer> videoIds = categoryVideoMaps.get(longVideoCategoryRelationEntity.getCategoryId());
            if (videoIds == null) {
                videoIds = new ArrayList<>();
            }
            videoIds.add(longVideoCategoryRelationEntity.getVideoId());
            categoryVideoMaps.put(longVideoCategoryRelationEntity.getCategoryId(), videoIds);
        }
        //TODO 标签Map
    }

    @Override
    public Map<String, Map<String, List<Integer>>> getIndex() {
        Map<String, Map<String, List<Integer>>> indexMap = new HashMap<>(3);
        indexMap.put("标签索引", tagVideoMaps);
        indexMap.put("拼音索引", pinYinTitleVideoMaps);
        indexMap.put("标题文字索引", textTitleVideoMaps);
        return indexMap;
    }


    public static String getPingYin(String inputString) {
        //创建转换对象
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        //转换类型（大写or小写）
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //定义中文声调的输出格式
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //定义字符的输出格式
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);
        //转换为字节数组
        char[] input = inputString.trim().toCharArray();
        // 用StringBuffer（字符串缓冲）来接收处理的数据
        StringBuilder output = new StringBuilder();
        try {
            for (char c : input) {
                //判断是否是一个汉字字符
                if (Character.toString(c).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (temp.length >= 1) {
                        output.append(temp[0]);
                    }
                } else {
                    // 如果不是汉字字符，直接拼接
                    output.append(c);
                }
            }
        } catch (BadHanyuPinyinOutputFormatCombination e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    public static void main(String[] args) {
        TokenizerEngine engine = TokenizerUtil.createEngine();
        Result words = engine.parse("艺术欣赏");
        for (Word word : words) {
            System.out.println(word.getText());
        }
    }

}
