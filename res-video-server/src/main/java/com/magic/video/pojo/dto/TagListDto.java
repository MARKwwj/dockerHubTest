package com.magic.video.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TagListDto {

    private List<LongVideoTagInfoDto> children;
    private String tagName;
    private Integer index;
}
