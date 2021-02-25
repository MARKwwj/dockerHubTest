package com.magic.longvideo.service.system;

import com.magic.longvideo.pojo.dto.LongVideoAppVersionDto;

public interface AppVersionService {
    LongVideoAppVersionDto version(Integer equipmentType);
}
