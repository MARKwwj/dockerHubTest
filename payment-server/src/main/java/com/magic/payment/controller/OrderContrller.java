package com.magic.payment.controller;

import com.magic.framework.structs.JsonResult;
import com.magic.framework.utils.TokenUtil;
import com.magic.payment.pojo.vo.CreateOrderVo;
import com.magic.payment.pojo.vo.PayResultVo;
import com.magic.payment.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pay_service/order")
@Api(tags = "[创建订单，支付接口，查询支付结果]")
public class OrderContrller {

    private final OrderService orderService;

    public OrderContrller(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/create")
    @ApiOperation("创建订单")
    public JsonResult create(@RequestBody CreateOrderVo createOrderVo) {
        Long userId = TokenUtil.getUserIdByTokenFromHeader();
        return JsonResult.successMap("orderNumber", orderService.createOrder(userId, createOrderVo.getAppType(), createOrderVo.getChannelId(), createOrderVo.getCommodityId()));
    }

    @GetMapping("/pay/{orderNumber}")
    @ApiOperation("支付接口")
    public String pay(@PathVariable String orderNumber) {
        return orderService.pay(orderNumber);
    }

    @PostMapping("/pay_result")
    @ApiOperation("查询支付结果")
    public JsonResult payResult(@RequestBody PayResultVo payResultVo) {
        boolean flag = orderService.payResult(payResultVo.getOrderId());
        return JsonResult.success("result", flag);
    }


}
