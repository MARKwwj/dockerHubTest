package com.magic.longvideo.service.user;

import com.magic.longvideo.common.consts.UserVideoMapFieldName;
import com.magic.longvideo.pojo.dto.VideoInfoDto;
import com.magic.longvideo.pojo.dto.VideoPraiseDto;
import com.magic.longvideo.pojo.vo.VideoInfoVo;

import java.util.List;

public interface UserVideoService {

    List<VideoInfoDto> getVideoInfoFromUserVideoMapByFieldName(Long userId, UserVideoMapFieldName fieldName);

    boolean addVideoInfoToUserVideoMapByFieldName(Long userId, VideoInfoVo videoInfoVo, UserVideoMapFieldName fieldName);

    boolean removeVideoInfoFromUserVideoMapByFieldName(Long userId, List<Long> videoIds, UserVideoMapFieldName fieldName);

    VideoPraiseDto praise(Integer videoId, Long userId);

    void increaseVideoRankListScore(Object obj, int videoRankTypeKey);

    void updateUserCoinDetails(Long userId, String videoTitle, Integer videoPayCoin);
}
