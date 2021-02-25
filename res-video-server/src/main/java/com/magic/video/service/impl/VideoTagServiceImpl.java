package com.magic.video.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.magic.framework.utils.MagicBeanUtil;
import com.magic.video.mapper.LongVideoTagInfoMapper;
import com.magic.video.pojo.dto.LongVideoTagInfoDto;
import com.magic.video.pojo.dto.TagListDto;
import com.magic.video.pojo.entity.LongVideoTagInfoEntity;
import com.magic.video.service.VideoTagService;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
public class VideoTagServiceImpl implements VideoTagService {


    private final LongVideoTagInfoMapper longVideoTagInfoMapper;

    public VideoTagServiceImpl(LongVideoTagInfoMapper longVideoTagInfoMapper) {
        this.longVideoTagInfoMapper = longVideoTagInfoMapper;
    }

    @Override
    public List<TagListDto> getTagAll() {
        QueryWrapper<LongVideoTagInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("tag_order");
        List<LongVideoTagInfoEntity> tagList = longVideoTagInfoMapper.selectList(queryWrapper);
        List<LongVideoTagInfoEntity> parentTagList = new ArrayList<>();
        for (int index = 0; index < tagList.size(); index++) {
            if (tagList.get(index).getParentId() == 0) {
                parentTagList.add(tagList.remove(index--));
            }
        }
        List<TagListDto> tagListDtoList = new ArrayList<>();
        for (int index = 0; index < parentTagList.size(); index++) {
            LongVideoTagInfoEntity parentTag = parentTagList.get(index);
            TagListDto tagListDto = new TagListDto().setIndex(index).setTagName(parentTag.getTagName());
            List<LongVideoTagInfoDto> childrenTags = new ArrayList<>();
            for (int i = 0; i < tagList.size(); i++) {
                LongVideoTagInfoEntity childTag = tagList.get(i);
                if (childTag.getParentId().equals(parentTag.getTagId())) {
                    LongVideoTagInfoDto childTagDto = new LongVideoTagInfoDto();
                    MagicBeanUtil.copyProperties(tagList.remove(i--), childTagDto);
                    childrenTags.add(childTagDto);
                }
            }
            tagListDto.setChildren(childrenTags);
            tagListDtoList.add(tagListDto);
        }
        return tagListDtoList;
    }

}
