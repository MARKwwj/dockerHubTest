package com.magic.video.service;

import com.magic.framework.structs.JsonResult;
import com.magic.video.pojo.dto.*;
import com.magic.video.pojo.vo.VideoCollectionVo;
import com.magic.video.pojo.vo.VideoFilterParamsVo;
import com.magic.video.pojo.vo.VideoSearchVo;

import java.util.List;
import java.util.Map;

public interface VideoService {
    LongVideoInfoDto info(Integer videoId);
    Map<String, Object> collection(VideoCollectionVo videoCollectionVo);
    Map<String, Object> filter(VideoFilterParamsVo videoFilterParamsVo);
    List<LongVideoClassifyInfoDto> classify(Integer categoryId);
    Map<String, Object> newest(NewestVideoVo newestVideoVo);
    JsonResult recommend(VideoSearchVo videoSearchVo, Long userId);
}
