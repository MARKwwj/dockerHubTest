package com.magic.search.controller;

import com.magic.framework.structs.JsonResult;
import com.magic.search.pojo.vo.VideoSearchVo;
import com.magic.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search_service/search")
@Api(tags = "[任务列表，任务进度增长(用于观看时长任务)，获取奖励]")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping
    @ApiOperation("搜索")
    public JsonResult search(@RequestBody VideoSearchVo videoSearchvo) {
        return JsonResult.success(searchService.search(videoSearchvo));
    }


    @PostMapping("/refresh")
    @ApiOperation("刷新索引")
    public JsonResult refresh() {
        searchService.refreshVideoMaps();
        return JsonResult.success();
    }

    @PostMapping("/index")
    public JsonResult getIndex(){
        return JsonResult.success(searchService.getIndex());
    }
}
