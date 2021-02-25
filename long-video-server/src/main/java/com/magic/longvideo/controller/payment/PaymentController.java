package com.magic.longvideo.controller.payment;

import com.magic.framework.structs.JsonResult;
import com.magic.longvideo.service.payment.PaymentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/long_video/payment")
@Api(tags = "[支付方式]")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay_method")
    @ApiOperation("根据商品ID获取支付方式")
    public JsonResult payMethod(@RequestBody Map<String,Integer> commodityId ) {
        return JsonResult.successMap("payMethods", paymentService.payMethod(commodityId.get("commodityId")));
    }

}
