package com.magic.longvideo.service.system;

import com.magic.longvideo.pojo.dto.LongVideoAdvertisingInfoDto;

import java.util.List;
import java.util.Map;

public interface AdvertisingService {
    Map<String, List<LongVideoAdvertisingInfoDto>> advertisingList();

    List<LongVideoAdvertisingInfoDto> specify(String adConfigKey);
}
