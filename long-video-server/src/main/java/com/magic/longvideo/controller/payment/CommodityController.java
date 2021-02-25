package com.magic.longvideo.controller.payment;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.payment.CommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/long_video/commodity")
@Api(tags = "[商品列表]")
public class CommodityController {

    private final CommodityService commodityService;

    public CommodityController(CommodityService commodityService) {
        this.commodityService = commodityService;
    }

    @ApiOperation("获取商品列表")
    @PostMapping("/info/{commodityType}")
    public JsonResult info(@ApiParam(value = "获取指定类型商品列表|1-VIP商品列表|2-金币商品   列表", example = "1") @PathVariable("commodityType") int commodityType) {
        return JsonResult.successMap("commodityInfos", commodityService.info(commodityType));
    }

}
