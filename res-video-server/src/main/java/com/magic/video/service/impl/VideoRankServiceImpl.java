package com.magic.video.service.impl;

import cn.hutool.json.JSONUtil;
import com.magic.framework.redis.JedisUtil;
import com.magic.video.common.consts.VideoRankDateTypePrefix;
import com.magic.video.common.consts.VideoRankType;
import com.magic.video.pojo.dto.RankListDto;
import com.magic.video.pojo.dto.RankListSearchInfoDto;
import com.magic.video.pojo.dto.RankListVideoInfoDto;
import com.magic.video.service.VideoRankService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Tuple;

import java.util.*;

@Service
public class VideoRankServiceImpl implements VideoRankService {

    /**
     * 这个map中保存排行榜中数据的排名值
     * 搜索排行榜保存的key为搜索的关键字
     * 视频相关排行榜保存的key是视频id
     */
    private Map<String, Integer> map = new HashMap<>();

    @Override
    public RankListDto getVideoRankListByRankType(int rankType, int rankDateType) {
        String suffix = VideoRankType.getValueByKey(rankType);
        String prefix = VideoRankDateTypePrefix.getValueByKey(rankDateType);
        String rankListName = prefix + suffix;
        Set<Tuple> rankList = JedisUtil.zRevRangeWithScores(rankListName, 0, 100);
        List<Object> infos = new ArrayList<>();
        int rankIndex = 1;
        for (Tuple tuple : rankList) {
            switch (rankType) {
                case 6:
                    RankListSearchInfoDto rankListSearchInfoDto = JSONUtil.toBean(tuple.getElement(), RankListSearchInfoDto.class);
                    Integer beforeRankIndex = map.get(rankListSearchInfoDto.getSearchValue());
                    int rankStatus = beforeRankIndex.equals(rankIndex)  ? 1 : beforeRankIndex > rankIndex ? 2 : 0;
                    infos.add(rankListSearchInfoDto.setRankScore((int) tuple.getScore()).setRankIndex(rankIndex).setStatus(rankStatus));
                    map.put(rankListSearchInfoDto.getSearchValue(), rankIndex);
                    break;
                default:
                    RankListVideoInfoDto rankListVideoInfoDto = JSONUtil.toBean(tuple.getElement(), RankListVideoInfoDto.class);
                    beforeRankIndex = map.get(rankListVideoInfoDto.getVideoId());
                    beforeRankIndex = beforeRankIndex == null ? 0 : beforeRankIndex;
                    rankStatus = beforeRankIndex == rankIndex ? 1 : beforeRankIndex > rankIndex ? 2 : 0;
                    infos.add(rankListVideoInfoDto.setRankScore((int) tuple.getScore()).setRankIndex(rankIndex).setStatus(rankStatus));
                    map.put(String.valueOf(rankListVideoInfoDto.getVideoId()), rankIndex);
            }
            if (++rankIndex > 30) {
                break;
            }
        }
        RankListDto rankListDto = new RankListDto();
        rankListDto.setInfos(infos).setRankListName(rankListName);
        return rankListDto;
    }
}
