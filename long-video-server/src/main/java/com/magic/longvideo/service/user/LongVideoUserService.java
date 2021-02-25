package com.magic.longvideo.service.user;

import com.magic.longvideo.pojo.dto.UserInfoDto;
import com.magic.longvideo.pojo.vo.UserJoinVo;

public interface LongVideoUserService {
    String join(UserJoinVo userJoinVo);

    String login(String machineCode, Integer appId);

    UserInfoDto info();

}
