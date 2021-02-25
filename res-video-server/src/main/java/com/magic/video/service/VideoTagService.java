package com.magic.video.service;

import com.magic.video.pojo.dto.TagListDto;

import java.util.List;

public interface VideoTagService {
    List<TagListDto> getTagAll();
}
