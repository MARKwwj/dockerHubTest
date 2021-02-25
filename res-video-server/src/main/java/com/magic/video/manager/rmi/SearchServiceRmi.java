package com.magic.video.manager.rmi;

import com.magic.framework.structs.JsonResult;
import com.magic.video.pojo.vo.VideoSearchVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("search-server")
public interface SearchServiceRmi {

    @PostMapping(value = "/search_service/search")
    JsonResult search(VideoSearchVo videoSearchvo);
}
