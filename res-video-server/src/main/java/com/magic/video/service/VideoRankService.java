package com.magic.video.service;

import com.magic.video.pojo.dto.RankListDto;

public interface VideoRankService {
    RankListDto getVideoRankListByRankType(int rankType, int rankDateType);
}
