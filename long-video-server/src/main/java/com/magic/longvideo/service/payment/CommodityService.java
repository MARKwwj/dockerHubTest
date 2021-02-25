package com.magic.longvideo.service.payment;

import com.magic.longvideo.pojo.dto.LongVideoCommodityInfoDto;

import java.util.List;


public interface CommodityService {
    List<LongVideoCommodityInfoDto> info(int commodityType);
}
