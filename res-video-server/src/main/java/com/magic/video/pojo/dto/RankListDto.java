package com.magic.video.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RankListDto {

    private String rankListName;
    private List<Object> infos;
}
