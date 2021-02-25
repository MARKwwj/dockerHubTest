package com.magic.longvideo.service.live;

import com.magic.longvideo.pojo.vo.LiveListVo;

import java.util.Map;

public interface LiveService {
    Map<String, Object> liveList(LiveListVo liveListVo);

    Long watched(Integer time, Long userId);
}
