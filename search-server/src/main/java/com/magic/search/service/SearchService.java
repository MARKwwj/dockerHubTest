package com.magic.search.service;

import com.magic.search.pojo.vo.VideoSearchVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SearchService {
    HashMap<String, Object> search(VideoSearchVo videoSearchVo);

    void refreshVideoMaps();

    Map<String, Map<String, List<Integer>>> getIndex();
}
