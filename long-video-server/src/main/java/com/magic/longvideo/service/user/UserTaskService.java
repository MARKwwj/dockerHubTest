package com.magic.longvideo.service.user;

import com.magic.longvideo.pojo.dto.UserTaskListDto;

import java.util.List;

public interface UserTaskService {
    List<UserTaskListDto> info();

    boolean increaseTaskProgressById(Integer taskMode, Integer taskType, int increaseNum);

    String receiveAward(Integer taskId, Integer taskType);
}
