package com.magic.video.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LongVideoClassifyInfoDto {

    private Integer id;
    private String name;
    private Integer categoryId;

}
