package com.magic.longvideo.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class UserTaskListDto {
    private int index;
    private String taskName;
    private List<LongVideoTaskInfoDto> taskList;
}
